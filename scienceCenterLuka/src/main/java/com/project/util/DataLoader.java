package com.project.util;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.project.model.Article;
import com.project.model.EditorReviewerByScienceArea;
import com.project.model.Magazine;
import com.project.model.MagazineEdition;
import com.project.model.ScienceArea;
import com.project.model.Term;
import com.project.model.enums.ArticleStatus;
import com.project.model.enums.Role;
import com.project.model.enums.WayOfPayment;
import com.project.model.user.UserSignedUp;
import com.project.model.user.tx.Membership;
import com.project.repository.UnityOfWork;

@Component
public class DataLoader implements ApplicationRunner {

	@Autowired
	private UnityOfWork unityOfWork;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
//	@Autowired
//	private DoiGeneratorRepository doiGeneratorRepository;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		initTermData();
		initScienceArea();
		initUsers();
		
		initMagazines();
		
		initMemberships();
		
		//initArticles();
		test();
	}
	
	private void initTermData() {
		Term term1 = Term.builder().termName("SCIENCE").build();
		Term term2 = Term.builder().termName("DATA MINING").build();
		Term term3 = Term.builder().termName("AI").build();
		Term term4 = Term.builder().termName("ICT").build();
		Term term5 = Term.builder().termName("OWASP").build();
		Term term6 = Term.builder().termName("NLP").build();
		Term term7 = Term.builder().termName("PCI DSS").build();
		Term term8 = Term.builder().termName("BUSINESS PROCESS").build();
		Term term9 = Term.builder().termName("CODE GENERATORS").build();
		Term term10 = Term.builder().termName("MDE").build();

		
		unityOfWork.getTermRepository().save(term1);
		unityOfWork.getTermRepository().save(term2);
		unityOfWork.getTermRepository().save(term3);
		unityOfWork.getTermRepository().save(term4);
		unityOfWork.getTermRepository().save(term5);
		unityOfWork.getTermRepository().save(term6);
		unityOfWork.getTermRepository().save(term7);
		unityOfWork.getTermRepository().save(term8);
		unityOfWork.getTermRepository().save(term9);
		unityOfWork.getTermRepository().save(term10);

	}
	
	private void initScienceArea() {
		ScienceArea scArea1 = ScienceArea.builder().scienceAreaName("Computer Science").scienceAreaCode("300").build();
		ScienceArea scArea2 = ScienceArea.builder().scienceAreaName("Artificial Intelligence").scienceAreaCode("301").build();
		ScienceArea scArea3 = ScienceArea.builder().scienceAreaName("Computer Programming").scienceAreaCode("302").build();
		ScienceArea scArea4 = ScienceArea.builder().scienceAreaName("Information System").scienceAreaCode("308").build();
		ScienceArea scArea5 = ScienceArea.builder().scienceAreaName("Architecture").scienceAreaCode("120").build();
		ScienceArea scArea6 = ScienceArea.builder().scienceAreaName("Agriculture").scienceAreaCode("100").build();
		ScienceArea scArea7 = ScienceArea.builder().scienceAreaName("Business Managment").scienceAreaCode("200").build();
		ScienceArea scArea8 = ScienceArea.builder().scienceAreaName("Pulmonary diseases").scienceAreaCode("6021").build();

		unityOfWork.getScienceAreaRepository().save(scArea1);
		unityOfWork.getScienceAreaRepository().save(scArea2);
		unityOfWork.getScienceAreaRepository().save(scArea3);
		unityOfWork.getScienceAreaRepository().save(scArea4);
		unityOfWork.getScienceAreaRepository().save(scArea5);
		unityOfWork.getScienceAreaRepository().save(scArea6);
		unityOfWork.getScienceAreaRepository().save(scArea7);
		unityOfWork.getScienceAreaRepository().save(scArea8);
	}
	
	private void initUsers() {
		UserSignedUp user0 = UserSignedUp.builder()
				.firstName("Luka")
				.lastName("Autorovic")
				.activatedAccount(true)
				.city("Novi Sad")
				.country("Serbia")
				.email("lukajvnv@gmail.com")
				.vocation("dipling")
				.role(Role.COMMON_USER)
				.password(passwordEncoder.encode("lukaAuthor"))
				.latitude(44.79d)		//bg
				.longitude(20.45d)
				.userUsername("lukaAuthor").build();
		
		UserSignedUp user01 = UserSignedUp.builder()
				.firstName("Nikola")
				.lastName("Autorovic")
				.activatedAccount(true)
				.city("Beograd")
				.country("Serbia")
				.email("lukajvnv@gmail.com")
				.vocation("dipling")
				.role(Role.COMMON_USER)
				.password(passwordEncoder.encode("nikolaAuthor"))
				.userUsername("nikolaAuthor").build();
		
		// editori
		
		UserSignedUp user1 = UserSignedUp.builder()
				.firstName("Petar")
				.lastName("Editor")
				.activatedAccount(true)
				.city("Novi Sad")
				.country("Serbia")
				.email("lukajvnv@gmail.com")
				.vocation("dipling")
				.role(Role.EDITOR)
				.wantToReviewe(true)
				.password(passwordEncoder.encode("editorDemo"))
				.userUsername("editorDemo").build();
		
		UserSignedUp user2 = UserSignedUp.builder()
				.firstName("Nikola")
				.lastName("Editor1")
				.activatedAccount(true)
				.city("Novi Sad")
				.country("Serbia")
				.email("lukajvnv@gmail.com")
				.vocation("dipling")
				.role(Role.EDITOR)
				.wantToReviewe(true)
				.password(passwordEncoder.encode("editorDemo1"))
				.userUsername("editorDemo1").build();
		
		UserSignedUp user3 = UserSignedUp.builder()
				.firstName("Maja")
				.lastName("Editor2")
				.activatedAccount(true)
				.city("Novi Sad")
				.country("Serbia")
				.email("lukajvnv@gmail.com")
				.vocation("dipling")
				.role(Role.EDITOR)
				.wantToReviewe(true)
				.password(passwordEncoder.encode("editorDemo2"))
				.userUsername("editorDemo2").build();
		
		UserSignedUp user4 = UserSignedUp.builder()
				.firstName("Miroslav")
				.lastName("Editor3")
				.activatedAccount(true)
				.city("Novi Sad")
				.country("Serbia")
				.email("lukajvnv@gmail.com")
				.vocation("dipling")
				.role(Role.EDITOR)
				.wantToReviewe(true)
				.password(passwordEncoder.encode("editorDemo3"))
				.userUsername("editorDemo3").build();
		
		UserSignedUp user5 = UserSignedUp.builder()
				.firstName("Marijana")
				.lastName("Editor4")
				.activatedAccount(true)
				.city("Novi Sad")
				.country("Serbia")
				.email("lukajvnv@gmail.com")
				.vocation("dipling")
				.role(Role.EDITOR)
				.wantToReviewe(true)
				.password(passwordEncoder.encode("editorDemo4"))
				.userUsername("editorDemo4").build();
		
		UserSignedUp user6 = UserSignedUp.builder()
				.firstName("Olga")
				.lastName("Editor5")
				.activatedAccount(true)
				.city("Novi Sad")
				.country("Serbia")
				.email("lukajvnv@gmail.com")
				.vocation("dipling")
				.role(Role.EDITOR)
				.wantToReviewe(true)
				.password(passwordEncoder.encode("editorDemo5"))
				.userUsername("editorDemo5").build();
		
		//recezenti
		List<Long> ids = Arrays.asList(new Long[] {1l,2l,3l,4l});
		List<ScienceArea> scAreasList = unityOfWork.getScienceAreaRepository().findAllById(ids);
		Set<ScienceArea> scAreasSet = new HashSet<ScienceArea>(scAreasList);
		
		UserSignedUp user7 = UserSignedUp.builder()
				.firstName("Luka")
				.lastName("Recezentovic")
				.activatedAccount(true)
				.city("Novi Sad")
				.country("Serbia")
				.email("lukajvnv@gmail.com")
				.vocation("dipling")
				.role(Role.REVIEWER)
				.wantToReviewe(true)
				.password(passwordEncoder.encode("reviewerDemo"))
				.userScienceAreas(scAreasSet)
				.latitude(44.79d)		//bg
				.longitude(20.45d)
				.userUsername("reviewerDemo").build();
		
		UserSignedUp user8 = UserSignedUp.builder()
				.firstName("Nikola")
				.lastName("Recezent 1")
				.activatedAccount(true)
				.city("Novi Sad")
				.country("Serbia")
				.email("lukajvnv@gmail.com")
				.vocation("dipling")
				.role(Role.REVIEWER)
				.wantToReviewe(true)
				.password(passwordEncoder.encode("reviewerDemo1"))
				.userScienceAreas(scAreasSet)
				.latitude(44.79d)		//bg
				.longitude(20.45d)
				.userUsername("reviewerDemo1").build();
		
		UserSignedUp user9 = UserSignedUp.builder()
				.firstName("Marko")
				.lastName("Recezent 2")
				.activatedAccount(true)
				.city("Novi Sad")
				.country("Serbia")
				.email("lukajvnv@gmail.com")
				.vocation("dipling")
				.role(Role.REVIEWER)
				.wantToReviewe(true)
				.password(passwordEncoder.encode("reviewerDemo2"))
				.userScienceAreas(scAreasSet)
				.latitude(45.27d)		//ns
				.longitude(19.83d)
				.userUsername("reviewerDemo2").build();
		
		UserSignedUp user10 = UserSignedUp.builder()
				.firstName("Blagoje")
				.lastName("Recezent 3")
				.activatedAccount(true)
				.city("Novi Sad")
				.country("Serbia")
				.email("lukajvnv@gmail.com")
				.vocation("dipling")
				.role(Role.REVIEWER)
				.wantToReviewe(true)
				.userScienceAreas(scAreasSet)
				.password(passwordEncoder.encode("reviewerDemo3"))
				.latitude(43.32d)		//bg
				.longitude(21.89d)
				.userUsername("reviewerDemo3").build();
		
		UserSignedUp user11 = UserSignedUp.builder()
				.firstName("Ostoja")
				.lastName("Recezent 4")
				.activatedAccount(true)
				.city("Novi Sad")
				.country("Serbia")
				.email("lukajvnv@gmail.com")
				.vocation("dipling")
				.role(Role.REVIEWER)
				.wantToReviewe(true)
				.userScienceAreas(scAreasSet)
				.password(passwordEncoder.encode("reviewerDemo4"))
				.latitude(44.79d)		//bg
				.longitude(20.45d)
				.userUsername("reviewerDemo4").build();
		
		UserSignedUp user12 = UserSignedUp.builder()
				.firstName("Mirjana")
				.lastName("Recezent 5")
				.activatedAccount(true)
				.city("Novi Sad")
				.country("Serbia")
				.email("lukajvnv@gmail.com")
				.vocation("dipling")
				.role(Role.REVIEWER)
				.wantToReviewe(true)
				.userScienceAreas(scAreasSet)
				.password(passwordEncoder.encode("reviewerDemo5"))
				.latitude(44.79d)		//bg
				.longitude(20.45d)
				.userUsername("reviewerDemo5").build();
		
		// ADMIN
		UserSignedUp admin = UserSignedUp.builder()
				.firstName("Admin")
				.lastName("Adminovic glavni")
				.activatedAccount(true)
				.city("Novi Sad")
				.country("Serbia")
				.email("lukajvnv@gmail.com")
				.vocation("dipling")
				.role(Role.ADMIN)
				.wantToReviewe(true)
				//.userScienceAreas(scAreasSet)
				.password(passwordEncoder.encode("demo"))
				.userUsername("demo").build();
		
		unityOfWork.getUserSignedUpRepository().save(user0);
		unityOfWork.getUserSignedUpRepository().save(user01);
		unityOfWork.getUserSignedUpRepository().save(user1);
		unityOfWork.getUserSignedUpRepository().save(user2);
		unityOfWork.getUserSignedUpRepository().save(user3);
		unityOfWork.getUserSignedUpRepository().save(user4);
		unityOfWork.getUserSignedUpRepository().save(user5);
		unityOfWork.getUserSignedUpRepository().save(user6);
		unityOfWork.getUserSignedUpRepository().save(user7);
		unityOfWork.getUserSignedUpRepository().save(user8);
		unityOfWork.getUserSignedUpRepository().save(user9);
		unityOfWork.getUserSignedUpRepository().save(user10);
		unityOfWork.getUserSignedUpRepository().save(user11);
		unityOfWork.getUserSignedUpRepository().save(user12);	
		unityOfWork.getUserSignedUpRepository().save(admin);		

	}
	
	private void initMagazines() {
		UserSignedUp chiefEditor = unityOfWork.getUserSignedUpRepository().findByUserUsername("editorDemo");
		List<ScienceArea> scienceAreas = unityOfWork.getScienceAreaRepository().findAllById(Arrays.asList(new Long[] {1l, 2l}));
		ScienceArea computerScience = scienceAreas.get(0);
		ScienceArea artificialIntelligence = scienceAreas.get(1);
		
		UserSignedUp computerScienceEditor = unityOfWork.getUserSignedUpRepository().findByUserUsername("editorDemo1");
		UserSignedUp artificialInteligenceEditor = unityOfWork.getUserSignedUpRepository().findByUserUsername("editorDemo2");
		
		UserSignedUp computerScienceReviewer1 = unityOfWork.getUserSignedUpRepository().findByUserUsername("reviewerDemo1");
		UserSignedUp computerScienceReviewer2 = unityOfWork.getUserSignedUpRepository().findByUserUsername("reviewerDemo2");
		UserSignedUp artificialIntelligence1 = unityOfWork.getUserSignedUpRepository().findByUserUsername("reviewerDemo3");

		//magazine1
		Magazine magazine1 = Magazine.builder()
									.active(true)
									.ISSN("4563-1232")
									.membershipPrice(1250l)
									.name("Computer science informer")
									.wayOfPayment(WayOfPayment.PAID_ACCESS)
									.chiefEditor(chiefEditor)
									.scienceAreas(new HashSet<ScienceArea>(scienceAreas))
									.build();
		
		Magazine persistedMagazine1 = unityOfWork.getMagazineRepository().save(magazine1);
		
		MagazineEdition magazineEdition1 = MagazineEdition.builder()
													.magazineEditionPrice(100f)
													.publishingDate(new Date())
													.magazine(persistedMagazine1)
													.build();
		
		MagazineEdition magazineEdition2 = MagazineEdition.builder()
				.magazineEditionPrice(200f)
				.publishingDate(new Date(119, 11, 30))  //2019 godina
				.magazine(persistedMagazine1)
				.build();
		
		unityOfWork.getMagazineEditionRepository().save(magazineEdition1);
		unityOfWork.getMagazineEditionRepository().save(magazineEdition2);
		
		
		EditorReviewerByScienceArea editor1 = EditorReviewerByScienceArea.builder()
																		.editor(true)
																		.magazine(persistedMagazine1)
																		.scienceArea(computerScience)
																		.editorReviewer(computerScienceEditor)
																		.build();
		
		EditorReviewerByScienceArea editor2 = EditorReviewerByScienceArea.builder()
																		.editor(true)
																		.magazine(persistedMagazine1)
																		.scienceArea(artificialIntelligence)
																		.editorReviewer(artificialInteligenceEditor)
																		.build();
		
		EditorReviewerByScienceArea reviewer1 = EditorReviewerByScienceArea.builder()
																		.editor(false)
																		.magazine(persistedMagazine1)
																		.scienceArea(computerScience)
																		.editorReviewer(computerScienceReviewer1)
																		.build();
		
		EditorReviewerByScienceArea reviewer2 = EditorReviewerByScienceArea.builder()
																		.editor(false)
																		.magazine(persistedMagazine1)
																		.scienceArea(computerScience)
																		.editorReviewer(computerScienceReviewer2)
																		.build();
		
		EditorReviewerByScienceArea reviewer3 = EditorReviewerByScienceArea.builder()
																		.editor(false)
																		.magazine(persistedMagazine1)
																		.scienceArea(artificialIntelligence)
																		.editorReviewer(artificialIntelligence1)
																		.build();
		
		unityOfWork.getEditorReviewerByScienceAreaRepository().save(editor1);
		unityOfWork.getEditorReviewerByScienceAreaRepository().save(editor2);
		unityOfWork.getEditorReviewerByScienceAreaRepository().save(reviewer1);
		unityOfWork.getEditorReviewerByScienceAreaRepository().save(reviewer2);
		unityOfWork.getEditorReviewerByScienceAreaRepository().save(reviewer3);

		
		
		
		// ************************ magazine2  *****************************************
		UserSignedUp chiefEditor2 = unityOfWork.getUserSignedUpRepository().findByUserUsername("editorDemo5");
		List<ScienceArea> scienceAreas2 = unityOfWork.getScienceAreaRepository().findAllById(Arrays.asList(new Long[] {4l, 7l}));
		ScienceArea ITSystem = scienceAreas.get(0);
		ScienceArea businessManagment = scienceAreas.get(1);
		
		UserSignedUp ITSystemEditor = unityOfWork.getUserSignedUpRepository().findByUserUsername("editorDemo3");
		UserSignedUp businessManagmentEditor = unityOfWork.getUserSignedUpRepository().findByUserUsername("editorDemo4");
		
		UserSignedUp ITSystemReviewer1 = unityOfWork.getUserSignedUpRepository().findByUserUsername("reviewerDemo1");
		UserSignedUp ITSystemReviewer2 = unityOfWork.getUserSignedUpRepository().findByUserUsername("reviewerDemo4");
		UserSignedUp businessManagmentReviewer1 = unityOfWork.getUserSignedUpRepository().findByUserUsername("reviewerDemo3");
		
		Magazine magazine2 = Magazine.builder()
									.active(true)
									.ISSN("4563-4425")
									.membershipPrice(1250l)
									.name("Business Managment")
									.wayOfPayment(WayOfPayment.OPEN_ACCESS)
									.chiefEditor(chiefEditor2)
									.scienceAreas(new HashSet<ScienceArea>(scienceAreas2))
									.build();
		
		Magazine persistedMagazine2 = unityOfWork.getMagazineRepository().save(magazine2);
		
		MagazineEdition magazineEdition21 = MagazineEdition.builder()
													.magazineEditionPrice(100f)
													.publishingDate(new Date())
													.magazine(persistedMagazine2)
													.build();
		
		MagazineEdition magazineEdition22 = MagazineEdition.builder()
				.magazineEditionPrice(200f)
				.publishingDate(new Date(119, 11, 30))  //2019 godina
				.magazine(persistedMagazine2)
				.build();
		
		unityOfWork.getMagazineEditionRepository().save(magazineEdition21);
		unityOfWork.getMagazineEditionRepository().save(magazineEdition22);
		
		
		EditorReviewerByScienceArea editor21 = EditorReviewerByScienceArea.builder()
																		.editor(true)
																		.magazine(persistedMagazine2)
																		.scienceArea(ITSystem)
																		.editorReviewer(ITSystemEditor)
																		.build();
		
		EditorReviewerByScienceArea editor22 = EditorReviewerByScienceArea.builder()
																		.editor(true)
																		.magazine(persistedMagazine2)
																		.scienceArea(businessManagment)
																		.editorReviewer(businessManagmentEditor)
																		.build();
		
		EditorReviewerByScienceArea reviewer21 = EditorReviewerByScienceArea.builder()
																		.editor(false)
																		.magazine(persistedMagazine2)
																		.scienceArea(ITSystem)
																		.editorReviewer(ITSystemReviewer1)
																		.build();
		
		EditorReviewerByScienceArea reviewer22 = EditorReviewerByScienceArea.builder()
																		.editor(false)
																		.magazine(persistedMagazine2)
																		.scienceArea(ITSystem)
																		.editorReviewer(ITSystemReviewer2)
																		.build();
		
		EditorReviewerByScienceArea reviewer23 = EditorReviewerByScienceArea.builder()
																		.editor(false)
																		.magazine(persistedMagazine2)
																		.scienceArea(businessManagment)
																		.editorReviewer(businessManagmentReviewer1)
																		.build();
		
		unityOfWork.getEditorReviewerByScienceAreaRepository().save(editor21);
		unityOfWork.getEditorReviewerByScienceAreaRepository().save(editor22);
		unityOfWork.getEditorReviewerByScienceAreaRepository().save(reviewer21);
		unityOfWork.getEditorReviewerByScienceAreaRepository().save(reviewer22);
		unityOfWork.getEditorReviewerByScienceAreaRepository().save(reviewer23);
		
		
	}
	
	private void initMemberships() {
		Magazine magazine = unityOfWork.getMagazineRepository().getOne(2l);
		UserSignedUp subscriber1 = unityOfWork.getUserSignedUpRepository().findByUserUsername("lukaAuthor");
//		UserSignedUp subscriber2 = unityOfWork.getUserSignedUpRepository().findByUserUsername("nikolaAuthor");
		
		Membership mShip1 = Membership.builder()
				.price(100f)
				.startAt(new Date(119, 11, 30))
				.endAt(new Date(120, 2, 30))
				.magazine(magazine)
				.signedUpUser(subscriber1)
				.build();
		
//		Membership mShip2 = Membership.builder()
//				.price(100f)
//				.startAt(new Date(119, 11, 30))
//				.endAt(new Date(119, 11, 30))
//				.magazine(magazine)
//				.signedUpUser(subscriber2)
//				.build();
		
		unityOfWork.getMembershipRepository().save(mShip1);
	}
	
	private void initArticles() {
		ScienceArea scienceArea = unityOfWork.getScienceAreaRepository().getOne(1l);
		Set<Term> keyTerms = unityOfWork.getTermRepository().findAllById(Arrays.asList(new Long[] {1l, 2l})).stream().collect(Collectors.toSet());
		UserSignedUp author = unityOfWork.getUserSignedUpRepository().findByUserUsername("lukaAuthor");
		MagazineEdition e = unityOfWork.getMagazineEditionRepository().getOne(1l);
		
		Article article = Article.builder()
								.articleTitle("WWW3 memes")
								.articleAbstract("Abstract")
								.articlePrice(500l)
								.status(ArticleStatus.ACCEPTED)
								.publishingDate(new Date())
								.scienceArea(scienceArea)
								.keyTerms(keyTerms)
								.author(author)
								.coAuthors(new HashSet<UserSignedUp>())
								.magazineEdition(e)
								.file(null)
								.fileFormat("")
								.build();
		
		Article a = unityOfWork.getArticleRepository().save(article);
//		DoiGenerator d = new DoiGenerator();
//		d.setArticle(a);
//		DoiGenerator dSaved = doiGeneratorRepository.save(d);
	}
	
	private void test() {
//		Magazine persistedMagazine2 = unityOfWork.getMagazineRepository().getOne(1l);
//		String t = persistedMagazine2.getName();
//		Set<EditorReviewerByScienceArea> list = persistedMagazine2.getEditorsReviewersByScienceArea();

	}
}
