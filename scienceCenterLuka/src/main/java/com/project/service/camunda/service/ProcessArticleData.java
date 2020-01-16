package com.project.service.camunda.service;


import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.ArticleProcessDto;
import com.project.dto.NewArticleResponseDto;
import com.project.dto.TermDto;
import com.project.dto.UserDto;
import com.project.model.Article;
import com.project.model.Magazine;
import com.project.model.MagazineEdition;
import com.project.model.OpinionAboutArticle;
import com.project.model.ScienceArea;
import com.project.model.Term;
import com.project.model.enums.ArticleStatus;
import com.project.model.enums.Role;
import com.project.model.user.UserSignedUp;
import com.project.repository.UnityOfWork;
import com.project.util.Base64Utility;

@Service
public class ProcessArticleData implements JavaDelegate {

	@Autowired
	private UnityOfWork unityOfWork;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		NewArticleResponseDto newArticleDto = (NewArticleResponseDto) execution.getVariable("newArticleDto");
		
		Long magazineId = (Long) execution.getVariable("select_magazine_id");
		Magazine magazine = unityOfWork.getMagazineRepository().getOne(magazineId);
		
		MagazineEdition latestEdition = unityOfWork.getMagazineEditionRepository().findByMagazineOrderByPublishingDateDesc(magazine).stream().findFirst().orElse(null);
		if (latestEdition == null) { }
		
		String userUsername = (String) execution.getVariable("user");
		UserSignedUp author = unityOfWork.getUserSignedUpRepository().findByUserUsername(userUsername);
		
		Long scienceId = Long.parseLong(newArticleDto.getArticleScienceArea());
		ScienceArea scienceArea = unityOfWork.getScienceAreaRepository().getOne(scienceId);
		
		List<TermDto> addedTermsDto = newArticleDto.getArticleTerm();
        List<Long> familiarTermsDto	= addedTermsDto.stream().filter(tDto -> tDto.getTermId() != null).map(TermDto::getTermId).collect(Collectors.toList());
        List<TermDto> newAddedTermsDto	= addedTermsDto.stream().filter(tDto -> tDto.getTermId() == null).collect(Collectors.toList());

		Set<Term> familiarTerms = unityOfWork.getTermRepository().findAllById(familiarTermsDto).stream().collect(Collectors.toSet());
        
		List<Term> newAddedTerms = new ArrayList<Term>();
		newAddedTermsDto.stream().forEach(tDto -> {
			Term t = new Term();
			t.setTermName(tDto.getTermName());
			newAddedTerms.add(t);
		});
		
		Set<Term> persistedNewAddedTerms = unityOfWork.getTermRepository().saveAll(newAddedTerms).stream().collect(Collectors.toSet());
		familiarTerms.addAll(persistedNewAddedTerms);
		
		List<UserDto> coauthorsDto = newArticleDto.getArticleCoAuthors();
		List<Long> familiarCoAuthors = coauthorsDto.stream().filter(uDto -> uDto.getUserId() != null).map(UserDto::getUserId).collect(Collectors.toList());
		List<UserDto> newAddedCoauthorsDto = coauthorsDto.stream().filter(uDto -> uDto.getUserId() == null).collect(Collectors.toList());
		
		Set<UserSignedUp> familiarCoauthors = unityOfWork.getUserSignedUpRepository().findAllById(familiarCoAuthors).stream().collect(Collectors.toSet());
		
		List<UserSignedUp> newAddedCoauthors = new ArrayList<UserSignedUp>();
		newAddedCoauthorsDto.stream().forEach(cDto -> {
			UserSignedUp u = UserSignedUp.builder()
									   .activatedAccount(true)
									   .firstName(cDto.getFirstName())
									   .lastName(cDto.getLastName())
									   .email(cDto.getEmail())
									   .city(cDto.getCity())
									   .country(cDto.getCountry())
									   .role(Role.OUT_OF_SYSTEM_USER)
									   .userUsername(cDto.getFirstName() + cDto.getLastName())
									   .build();
			newAddedCoauthors.add(u);
		});
		
		Set<UserSignedUp> persistedNewAddedCoauthors = unityOfWork.getUserSignedUpRepository().saveAll(newAddedCoauthors).stream().collect(Collectors.toSet());
		familiarCoauthors.addAll(persistedNewAddedCoauthors);
		
		String[] fileParts = newArticleDto.getFile().split(",");
		byte[] decodedByte = Base64Utility.decode(fileParts[1]);
		String fileFormat = fileParts[0];
		
		Article article = Article.builder()
								.articleTitle(newArticleDto.getArticleTitle())
								.articleAbstract(newArticleDto.getArticleAbstract())
								.articlePrice(newArticleDto.getArticlePrice())
								//.file(newArticleDto.getFile())   //value to long String(255)
								.fileFormat(fileFormat)
								.file(decodedByte)
								.magazineEdition(latestEdition)
								.author(author)
								.scienceArea(scienceArea)
								.keyTerms(familiarTerms)
								.coAuthors(familiarCoauthors)
								.publishingDate(new Date())
								.status(ArticleStatus.SUBMITTED)
								.build();
		
		ArticleProcessDto articleProcessIfUpdate = (ArticleProcessDto) execution.getVariable("articleProcessDto");
		if(articleProcessIfUpdate != null) {
			article.setArticleId(articleProcessIfUpdate.getArticleId());
			unityOfWork.getArticleRepository().save(article);
		} else {
			Article persistedArticle = unityOfWork.getArticleRepository().save(article);
			
			ArticleProcessDto articleProcessDto = new ArticleProcessDto(execution.getProcessInstanceId(), 
					magazineId, persistedArticle.getArticleId(), author.getUserUsername(), "", "",
					new ArrayList<String>(), new ArrayList<OpinionAboutArticle>(), new ArrayList<OpinionAboutArticle>() , new ArrayList<OpinionAboutArticle>(), 1);
			
			execution.setVariable("articleProcessDto", articleProcessDto);
		}
		
		
	}

}

