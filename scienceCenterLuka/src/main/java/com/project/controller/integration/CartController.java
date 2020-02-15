package com.project.controller.integration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.camunda.bpm.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.project.dto.ArticleDto;
import com.project.dto.MagazineEditionDto;
import com.project.dto.integration.NewCartItemRequest;
import com.project.dto.integration.ShoppingCartDto;
import com.project.dto.integration.TxInfoDto;
import com.project.dto.integration.UserTxDto;
import com.project.dto.integration.UserTxItemDto;
import com.project.model.Article;
import com.project.model.Magazine;
import com.project.model.MagazineEdition;
import com.project.model.enums.BuyingType;
import com.project.model.enums.TxStatus;
import com.project.model.user.UserSignedUp;
import com.project.model.user.tx.UserTx;
import com.project.model.user.tx.UserTxItem;
import com.project.repository.UnityOfWork;

@RestController
@RequestMapping("/cart")
@CrossOrigin
public class CartController {
	
	@Autowired
	private UnityOfWork unityOfWork;
	
	@Autowired
	private IdentityService identityService;
	
	@RequestMapping(path = "/addItemToCart", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MagazineEditionDto> addItemToCart(@RequestBody NewCartItemRequest request) {
		
		UserTx cartOrTx= unityOfWork.getUserTxRepository().getOne(request.getCartId());
		
		//if first item
		if(cartOrTx.getKPClientIdentifier() == null) {
			BuyingType type = request.getBuyingType();
			MagazineEdition edition = null;
			Magazine magazine = null;
			switch (type) {
				case AUTHORS_PARTICIPATION:
				case ARTICLE:
					Article article = unityOfWork.getArticleRepository().getOne(request.getArticleId());
					edition = article.getMagazineEdition();
					magazine = edition.getMagazine();
					break;
				
				case MAGAZINE_EDITION:
					edition = unityOfWork.getMagazineEditionRepository().getOne(request.getArticleId());
					magazine = edition.getMagazine();
					break;

			default:
				break;
			}
			
			cartOrTx.setKPClientIdentifier(magazine.getSellerIdentifier());
		} else {
			BuyingType type = request.getBuyingType();
			MagazineEdition edition = null;
			Magazine magazine = null;
			switch (type) {
				case AUTHORS_PARTICIPATION:
				case ARTICLE:
					Article article = unityOfWork.getArticleRepository().getOne(request.getArticleId());
					edition = article.getMagazineEdition();
					magazine = edition.getMagazine();
					break;
				
				case MAGAZINE_EDITION:
					edition = unityOfWork.getMagazineEditionRepository().getOne(request.getArticleId());
					magazine = edition.getMagazine();
					break;

			default:
				break;
			}
			
			//different kp client constraint
			if(!(magazine.getSellerIdentifier().equals(cartOrTx.getKPClientIdentifier()))) {
				System.out.println("razlicito");
				return new ResponseEntity<>(new MagazineEditionDto(), HttpStatus.CONFLICT);
			}
		}
		
		BuyingType type = request.getBuyingType();
		long itemId = -1l;
		float price = -1f;
		switch (type) {
			case AUTHORS_PARTICIPATION:
			case ARTICLE:
				Article article = unityOfWork.getArticleRepository().getOne(request.getArticleId());
				price = article.getArticlePrice();
				itemId = article.getArticleId();
				break;
			
			case MAGAZINE_EDITION:
				MagazineEdition edition = unityOfWork.getMagazineEditionRepository().getOne(request.getArticleId());
				price = edition.getMagazineEditionPrice();
				itemId = edition.getMagazineEditionId();
				break;

		default:
			break;
		}
		
		cartOrTx.setTotalAmount(cartOrTx.getTotalAmount() + price);
		UserTx cartOrTxUpdated = unityOfWork.getUserTxRepository().save(cartOrTx);
		
		UserTxItem newUserTxItem = UserTxItem.builder()
				.buyingType(type)
				.itemId(itemId)
				.price(price)
				.userTx(cartOrTxUpdated)
				.build();

		
		unityOfWork.getUserTxItemRepository().save(newUserTxItem);
		
		
		return new ResponseEntity<MagazineEditionDto>(new MagazineEditionDto(), HttpStatus.OK);
	}
	
	@RequestMapping(path = "/removeItemFromCart/{itemId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MagazineEditionDto> removeItemFromCart(@PathVariable long itemId) {
		
		UserTxItem itemToDelete = unityOfWork.getUserTxItemRepository().getOne(itemId);
		
		UserTx tx = itemToDelete.getUserTx();
		tx.setTotalAmount(tx.getTotalAmount() - itemToDelete.getPrice());
		unityOfWork.getUserTxRepository().save(tx);
		
		unityOfWork.getUserTxItemRepository().deleteById(itemId);
		
		return new ResponseEntity<MagazineEditionDto>(new MagazineEditionDto(), HttpStatus.OK);
	}
	
	@RequestMapping(path = "/newCart", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ShoppingCartDto> newCart() {
		
		String username = "";
		try {
			   username = identityService.getCurrentAuthentication().getUserId(); //ako nema puca exception
			} catch (Exception e) {
				return new ResponseEntity<ShoppingCartDto>(HttpStatus.CONFLICT);
		}
		
		//privremeno 
		UserSignedUp loggedUser = unityOfWork.getUserSignedUpRepository().findByUserUsername(username);
		if(loggedUser == null) {
			return new ResponseEntity<ShoppingCartDto>(HttpStatus.CONFLICT);
		}
				
		UserTx newTx = UserTx.builder()
				.user(loggedUser)
				.created(new Date())
				.status(TxStatus.UNKNOWN)
				.totalAmount(0f)
				.build();
		
		UserTx persistedNewTx = unityOfWork.getUserTxRepository().save(newTx);
		
		
		
		return new ResponseEntity<ShoppingCartDto>(new ShoppingCartDto(persistedNewTx.getUserTxId()), HttpStatus.OK);
	}
	
	@RequestMapping(path = "/getCart/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserTxDto> getCart(@PathVariable Long id) {
		
		 
		UserTx cart = unityOfWork.getUserTxRepository().getOne(id);
		
		Set<UserTxItem> cartItems = cart.getItems();
		
		UserTxDto cartDto = new UserTxDto(cart.getUserTxId(), cart.getCreated(), cart.getStatus(), cart.getTotalAmount());  
		List<UserTxItemDto> cartItemsDto = new ArrayList<UserTxItemDto>();
		
		for(UserTxItem item : cartItems) {
			cartItemsDto.add(new UserTxItemDto(item.getUserTxItemId(), item.getPrice(), cartDto, item.getBuyingType(), item.getItemId()));
		}
		
		cartDto.setItems(cartItemsDto);
		
		return new ResponseEntity<UserTxDto>(cartDto, HttpStatus.OK);
	}
	
	
	@RequestMapping(path = "/getUserTxs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserTxDto>> getUserTxs() {
		
		String username = "";
		try {
			   username = identityService.getCurrentAuthentication().getUserId(); //ako nema puca exception
			} catch (Exception e) {
				return new ResponseEntity<List<UserTxDto>>(HttpStatus.CONFLICT);
			}
		
		//privremeno 
		UserSignedUp loggedUser = unityOfWork.getUserSignedUpRepository().findByUserUsername(username);
		if(loggedUser == null) {
			return new ResponseEntity<List<UserTxDto>>(HttpStatus.CONFLICT);
		}
		
		List<UserTx> userTxs = loggedUser.getUserTxs().stream().filter(tx -> tx.getStatus() == TxStatus.SUCCESS).collect(Collectors.toList());
		List<UserTxDto> userTxDtos = new ArrayList<UserTxDto>();
		
		for(UserTx tx: userTxs) {
			
			Set<UserTxItem> cartItems = tx.getItems();
			
			UserTxDto cartDto = new UserTxDto(tx.getUserTxId(), tx.getCreated(), tx.getStatus(), tx.getTotalAmount());  
			List<UserTxItemDto> cartItemsDto = new ArrayList<UserTxItemDto>();
			
			for(UserTxItem item : cartItems) {
				
				Object content = null;
				
				switch (item.getBuyingType()) {
					case AUTHORS_PARTICIPATION: 
					case ARTICLE:
						Article a = unityOfWork.getArticleRepository().getOne(item.getItemId());
						ArticleDto aDto = ArticleDto.builder()
						.articleId(a.getArticleId())
						.articleTitle(a.getArticleTitle())
						.articleAbstract(a.getArticleAbstract())
						.publishingDate(a.getPublishingDate())
						.price(a.getArticlePrice())
						.doi(a.getDoi())
						.build();
						content = aDto;
						break;
					
					case MAGAZINE_EDITION:
						MagazineEdition edition = unityOfWork.getMagazineEditionRepository().getOne(item.getItemId());
						MagazineEditionDto dto = new MagazineEditionDto(edition.getMagazineEditionId(), edition.getPublishingDate(), edition.getMagazineEditionPrice(), edition.getTitle());
						content = dto;
						break;

					default:
						break;
				}
				
				

				UserTxItemDto d = new UserTxItemDto(item.getUserTxItemId(), item.getPrice(), cartDto, item.getBuyingType(), item.getItemId());
				d.setContent(content);
				
				cartItemsDto.add(d);
			}
			
			cartDto.setItems(cartItemsDto);
			
			userTxDtos.add(cartDto);

		}
		
				
		return new ResponseEntity<List<UserTxDto>>(userTxDtos, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/getUserTxsOther", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserTxDto>> getUserTxsOther() {
		
		String username = "";
		try {
			   username = identityService.getCurrentAuthentication().getUserId(); //ako nema puca exception
			} catch (Exception e) {
				return new ResponseEntity<List<UserTxDto>>(HttpStatus.CONFLICT);
			}
		
		//privremeno 
		UserSignedUp loggedUser = unityOfWork.getUserSignedUpRepository().findByUserUsername(username);
		if(loggedUser == null) {
			return new ResponseEntity<List<UserTxDto>>(HttpStatus.CONFLICT);
		}
		
		List<UserTx> userTxs = loggedUser.getUserTxs().stream().filter(tx -> tx.getStatus() != TxStatus.SUCCESS).collect(Collectors.toList());
		List<UserTxDto> userTxDtos = new ArrayList<UserTxDto>();
		
		for(UserTx tx: userTxs) {
			
			Set<UserTxItem> cartItems = tx.getItems();
			
			UserTxDto cartDto = new UserTxDto(tx.getUserTxId(), tx.getCreated(), tx.getStatus(), tx.getTotalAmount());  
			List<UserTxItemDto> cartItemsDto = new ArrayList<UserTxItemDto>();
			
			for(UserTxItem item : cartItems) {
				
				Object content = null;
				
				switch (item.getBuyingType()) {
					case AUTHORS_PARTICIPATION: 
					case ARTICLE:
						Article a = unityOfWork.getArticleRepository().getOne(item.getItemId());
						ArticleDto aDto = ArticleDto.builder()
						.articleId(a.getArticleId())
						.articleTitle(a.getArticleTitle())
						.articleAbstract(a.getArticleAbstract())
						.publishingDate(a.getPublishingDate())
						.price(a.getArticlePrice())
						.doi(a.getDoi())
						.build();
						content = aDto;
						break;
					
					case MAGAZINE_EDITION:
						MagazineEdition edition = unityOfWork.getMagazineEditionRepository().getOne(item.getItemId());
						MagazineEditionDto dto = new MagazineEditionDto(edition.getMagazineEditionId(), edition.getPublishingDate(), edition.getMagazineEditionPrice(), edition.getTitle());
						content = dto;
						break;

					default:
						break;
				}
				
				

				UserTxItemDto d = new UserTxItemDto(item.getUserTxItemId(), item.getPrice(), cartDto, item.getBuyingType(), item.getItemId());
				d.setContent(content);
				
				cartItemsDto.add(d);
			}
			
			cartDto.setItems(cartItemsDto);
			
			userTxDtos.add(cartDto);

		}
		
				
		return new ResponseEntity<List<UserTxDto>>(userTxDtos, HttpStatus.OK);
	}
	
	

}
