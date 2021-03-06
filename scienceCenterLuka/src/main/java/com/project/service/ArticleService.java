package com.project.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.model.Article;
import com.project.model.DoiGenerator;
import com.project.model.MagazineEdition;
import com.project.model.ScienceArea;
import com.project.model.Term;
import com.project.model.enums.ArticleStatus;
import com.project.model.user.UserSignedUp;
import com.project.repository.DoiGeneratorRepository;
import com.project.repository.UnityOfWork;
import com.project.util.Base64Utility;

@Service
public class ArticleService {

	@Autowired
	private UnityOfWork unityOfWork;
	
	@Autowired
	private DoiGeneratorRepository doiGeneratorRepository;
	
	public Article getArticle(Long articleId) {
		return unityOfWork.getArticleRepository().getOne(articleId);
	}
	
	public List<Article> findAll(){
		return unityOfWork.getArticleRepository().findAll();
	}
	
	public Article save(Article article) {
		return unityOfWork.getArticleRepository().save(article);
	}
	
	public String getDocument(Long articleId) {
		Article article = getArticle(articleId);
		
		String encoded = Base64Utility.encode(article.getFile());
		
//		 String ret = article.getFileFormat() + "," + encoded;
//		 String ret = "data:base64" + "," + encoded;
		 String ret = encoded;
		
		return ret;
	}
	
	public byte[] getFile(Long articleId) {
		Article article = getArticle(articleId);
		
		return article.getFile();
	}
	
	public void generateDoi() {
		initArticles();
	}
	
	private void initArticles() {
		ScienceArea scienceArea = unityOfWork.getScienceAreaRepository().getOne(1l);
		Set<Term> keyTerms = unityOfWork.getTermRepository().findAllById(Arrays.asList(new Long[] {1l, 2l})).stream().collect(Collectors.toSet());
		
		UserSignedUp lukaAuthor = unityOfWork.getUserSignedUpRepository().findByUserUsername("lukaAuthor");
		UserSignedUp nikolaAuthor = unityOfWork.getUserSignedUpRepository().findByUserUsername("nikolaAuthor");

		
		MagazineEdition e = unityOfWork.getMagazineEditionRepository().getOne(1l);
		
		Article article = Article.builder()
								.articleTitle("WWW3 memes")
								.articleAbstract("Abstract")
								.articlePrice(40l)
								.status(ArticleStatus.ACCEPTED)
								.publishingDate(new Date())
								.scienceArea(scienceArea)
								.keyTerms(keyTerms)
								.author(lukaAuthor)
								.coAuthors(new HashSet<UserSignedUp>())
								.magazineEdition(e)
								.file(null)
								.build();
		
		Article a = unityOfWork.getArticleRepository().save(article);
		DoiGenerator d = new DoiGenerator();
		d.setArticle(a);
		DoiGenerator dSaved = doiGeneratorRepository.save(d);
		
		String doi = generateDoi(dSaved);
		System.out.println(doi);
	}
	
	private String generateDoi(DoiGenerator d) {
		Integer b = d.getGeneratedId();
		String bString = b.toString();
		StringBuilder builder = new StringBuilder("10.1").append(bString).insert(7, '/');
		System.out.println(builder.toString());
		String ret = "10.1"; 
		
		return builder.toString();
	}
	
	
}
