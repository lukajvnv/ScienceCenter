package com.project.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnityOfWork {

	@Autowired
	private TermRepository termRepository;
	
	@Autowired
	private ScienceAreaRepository scienceAreaRepository;
	
	@Autowired
	private UserSignedUpRepository userSignedUpRepository;
	
	@Autowired
	private MagazineRepository magazineRepository;
	
	@Autowired
	private EditorReviewerByScienceAreaRepository editorReviewerByScienceAreaRepository;
	
	@Autowired
	private MagazineEditionRepository magazineEditionRepository;
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private MembershipRepository membershipRepository;
	
	
//	@Autowired
//	private UserActivationCodeRepository userActivationCodeRepository;

	public TermRepository getTermRepository() {
		return termRepository;
	}

	public ScienceAreaRepository getScienceAreaRepository() {
		return scienceAreaRepository;
	}

	public UserSignedUpRepository getUserSignedUpRepository() {
		return userSignedUpRepository;
	}

	public MagazineRepository getMagazineRepository() {
		return magazineRepository;
	}

	public EditorReviewerByScienceAreaRepository getEditorReviewerByScienceAreaRepository() {
		return editorReviewerByScienceAreaRepository;
	}

	public MagazineEditionRepository getMagazineEditionRepository() {
		return magazineEditionRepository;
	}

	public ArticleRepository getArticleRepository() {
		return articleRepository;
	}

	public MembershipRepository getMembershipRepository() {
		return membershipRepository;
	}
	
	
//	public UserActivationCodeRepository getUserActivationCodeRepository() {
//		return userActivationCodeRepository;
//	}
	
	
}
