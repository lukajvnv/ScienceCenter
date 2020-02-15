package com.project.service;

import java.util.Date;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.project.dto.integration.OrderIdDTO;
import com.project.dto.integration.ShoppingCartRequestKpDto;
import com.project.model.MagazineEdition;
import com.project.model.enums.BuyingType;
import com.project.model.enums.TxStatus;
import com.project.model.user.UserSignedUp;
import com.project.model.user.tx.UserTx;
import com.project.model.user.tx.UserTxItem;
import com.project.repository.UnityOfWork;

@Service
public class KpService {
	
	@Autowired
	private UnityOfWork unityOfWork;
	
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	public OrderIdDTO pay(String procId, MagazineEdition ed) throws Exception {
		
		String username = "";
		try {
		   username = identityService.getCurrentAuthentication().getUserId(); //ako nema puca exception
		} catch (Exception e) {
			throw new Exception();
		}
		
		UserSignedUp user = unityOfWork.getUserSignedUpRepository().findByUserUsername(username);

		UserTx authorSub = UserTx.builder()
				.created(new Date())
				.kPClientIdentifier(1l)
				.user(user)
				.status(TxStatus.UNKNOWN)
				.totalAmount(ed.getMagazineEditionPrice())
				.procId(procId)
				.build();
		
		UserTx persistedNewTx = unityOfWork.getUserTxRepository().save(authorSub);
	
		UserTxItem txItem = UserTxItem.builder()
				.buyingType(BuyingType.AUTHORS_PARTICIPATION)
				.price(ed.getMagazineEditionPrice())
				.userTx(persistedNewTx)
				.build();
		
		UserTxItem persistedItem = unityOfWork.getUserTxItemRepository().save(txItem);
		
		runtimeService.setVariable(procId, "itemTxId", persistedItem.getUserTxItemId());
		
		String url = "https://localhost:8762/requestHandler/request/save";

		ShoppingCartRequestKpDto kpRequest = new ShoppingCartRequestKpDto(persistedNewTx.getKPClientIdentifier(), persistedNewTx.getTotalAmount());
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<OrderIdDTO> dto = null;
		try {
			dto = restTemplate.exchange(url, HttpMethod.POST, createHeader(kpRequest), OrderIdDTO.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// za izvucenu tu tx iz baze dodeliti id iz kpA
		persistedNewTx.setOrderId(dto.getBody().getOrderId());
		unityOfWork.getUserTxRepository().save(persistedNewTx);
		
		return dto.getBody();
	}
	
	private HttpEntity<?> createHeader (Object body){
		HttpHeaders headers = new HttpHeaders();
		headers.add("external", "true");
		headers.add("hostsc", "localhost:" + 8085);
		
		HttpEntity<?> entity;
		if(body != null) {
			entity = new HttpEntity<>(body, headers);
		} else {
			entity = new HttpEntity<>(headers);
		}
		
		return entity;
	}

}
