package com.project.service.camunda.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import com.project.model.user.tx.UserTxItem;
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
		
		
		String article_title = (String) execution.getVariable("article_title");
		String article_abstract = (String) execution.getVariable("article_abstract");
		String article_science_area = (String) execution.getVariable("article_science_area");
		byte[] article_file = (byte[]) execution.getVariable("article_file");
		Long article_price = (Long) execution.getVariable("article_price");
		
		List<String> article_key_terms = (ArrayList<String>) execution.getVariable("article_key_terms");
		List<String> article_co_authors = (ArrayList<String>) execution.getVariable("article_co_authors");
		List<Map<String, String>> article_co_authors_out = (ArrayList<Map<String, String>>) execution.getVariable("article_co_authors_out");
		List<Map<String, String>> article_key_terms_out = (ArrayList<Map<String, String>>) execution.getVariable("article_key_terms_out");

				
		Long scienceId = Long.parseLong(article_science_area);
		ScienceArea scienceArea = unityOfWork.getScienceAreaRepository().getOne(scienceId);
		
		//terms
//		List<TermDto> addedTermsDto = newArticleDto.getArticleTerm();
//        List<Long> familiarTermsDto	= addedTermsDto.stream().filter(tDto -> tDto.getTermId() != null).map(TermDto::getTermId).collect(Collectors.toList());
//        List<TermDto> newAddedTermsDto	= addedTermsDto.stream().filter(tDto -> tDto.getTermId() == null).collect(Collectors.toList());
//
//		Set<Term> familiarTerms = unityOfWork.getTermRepository().findAllById(familiarTermsDto).stream().collect(Collectors.toSet());
//        
//		List<Term> newAddedTerms = new ArrayList<Term>();
//		newAddedTermsDto.stream().forEach(tDto -> {
//			Term t = new Term();
//			t.setTermName(tDto.getTermName());
//			newAddedTerms.add(t);
//		});
//		
//		Set<Term> persistedNewAddedTerms = unityOfWork.getTermRepository().saveAll(newAddedTerms).stream().collect(Collectors.toSet());
//		familiarTerms.addAll(persistedNewAddedTerms);
		
		//terms from fields
        List<Long> familiarTermsDto	= article_key_terms.stream().map(a -> a).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());

		Set<Term> familiarTerms = unityOfWork.getTermRepository().findAllById(familiarTermsDto).stream().collect(Collectors.toSet());
        
		List<Term> newAddedTerms = new ArrayList<Term>();
		article_key_terms_out.stream().forEach(tDto -> {
			Term t = new Term();
			t.setTermName(tDto.get("termName"));
			newAddedTerms.add(t);
		});
		
		Set<Term> persistedNewAddedTerms = unityOfWork.getTermRepository().saveAll(newAddedTerms).stream().collect(Collectors.toSet());
		familiarTerms.addAll(persistedNewAddedTerms);
		
//		//coauthors
//		List<UserDto> coauthorsDto = newArticleDto.getArticleCoAuthors();
//		List<Long> familiarCoAuthors = coauthorsDto.stream().filter(uDto -> uDto.getUserId() != null).map(UserDto::getUserId).collect(Collectors.toList());
//		List<UserDto> newAddedCoauthorsDto = coauthorsDto.stream().filter(uDto -> uDto.getUserId() == null).collect(Collectors.toList());
//		
//		Set<UserSignedUp> familiarCoauthors = unityOfWork.getUserSignedUpRepository().findAllById(familiarCoAuthors).stream().collect(Collectors.toSet());
//		
//		List<UserSignedUp> newAddedCoauthors = new ArrayList<UserSignedUp>();
//		newAddedCoauthorsDto.stream().forEach(cDto -> {
//			UserSignedUp u = UserSignedUp.builder()
//									   .activatedAccount(true)
//									   .firstName(cDto.getFirstName())
//									   .lastName(cDto.getLastName())
//									   .email(cDto.getEmail())
//									   .city(cDto.getCity())
//									   .country(cDto.getCountry())
//									   .role(Role.OUT_OF_SYSTEM_USER)
//									   .userUsername(cDto.getFirstName() + cDto.getLastName())
//									   .build();
//			newAddedCoauthors.add(u);
//		});
//		
//		Set<UserSignedUp> persistedNewAddedCoauthors = unityOfWork.getUserSignedUpRepository().saveAll(newAddedCoauthors).stream().collect(Collectors.toSet());
//		familiarCoauthors.addAll(persistedNewAddedCoauthors);
		
		//coauthors - from fieds
		List<Long> familiarCoAuthors = article_co_authors.stream().map(a -> a).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
		
		Set<UserSignedUp> familiarCoauthors = unityOfWork.getUserSignedUpRepository().findAllById(familiarCoAuthors).stream().collect(Collectors.toSet());
		
		List<UserSignedUp> newAddedCoauthors = new ArrayList<UserSignedUp>();
		article_co_authors_out.stream().forEach(cDto -> {
			UserSignedUp u = UserSignedUp.builder()
									   .activatedAccount(true)
									   .firstName(cDto.get("firstName"))
									   .lastName(cDto.get("lastName"))
									   .email(cDto.get("email"))
									   .city(cDto.get("city"))
									   .country(cDto.get("country"))
									   .role(Role.OUT_OF_SYSTEM_USER)
									   .userUsername(cDto.get("firstName") + cDto.get("lastName"))
									   .build();
			newAddedCoauthors.add(u);
		});
		
		Set<UserSignedUp> persistedNewAddedCoauthors = unityOfWork.getUserSignedUpRepository().saveAll(newAddedCoauthors).stream().collect(Collectors.toSet());
		familiarCoauthors.addAll(persistedNewAddedCoauthors);
		
		//file handling
//		String[] fileParts = newArticleDto.getFile().split(",");
//		String[] fileParts = article_file.split(",");
//		byte[] decodedByte = Base64Utility.decode(fileParts[1]);
//		String fileFormat = fileParts[0];
		
		Article article = Article.builder()
								.articleTitle(article_title)
								.articleAbstract(article_abstract)
								.articlePrice(article_price)
								//.file(newArticleDto.getFile())   //value to long String(255)
								.file(article_file)
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
			Article persisted = unityOfWork.getArticleRepository().save(article);
			
		} else {
			Article persistedArticle = unityOfWork.getArticleRepository().save(article);
			
			ArticleProcessDto articleProcessDto = new ArticleProcessDto(execution.getProcessInstanceId(), 
					magazineId, persistedArticle.getArticleId(), author.getUserUsername(), "", "",
					new ArrayList<String>(), new ArrayList<OpinionAboutArticle>(), new ArrayList<OpinionAboutArticle>() , new ArrayList<OpinionAboutArticle>(), 1);
			
			execution.setVariable("articleProcessDto", articleProcessDto);
			
			long itemTxId = (long) execution.getVariable("itemTxId");
			
			UserTxItem item = unityOfWork.getUserTxItemRepository().getOne(itemTxId);
			item.setItemId(persistedArticle.getArticleId());
			
			unityOfWork.getUserTxItemRepository().save(item);
		}
		
		
	}

}

