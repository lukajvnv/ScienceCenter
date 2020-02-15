package com.project.controller.integration;

import java.util.HashMap;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.project.dto.integration.MagazineDTO;
import com.project.dto.integration.NewClientResponse;
import com.project.dto.integration.NewMagazineConfirmationDto;
import com.project.dto.integration.OrderIdDTO;
import com.project.dto.integration.ShoppingCartDto;
import com.project.dto.integration.ShoppingCartRequestKpDto;
import com.project.dto.integration.TxInfoDto;
import com.project.model.Magazine;
import com.project.model.session.NewMagazineRequestSession;
import com.project.model.user.tx.UserTx;
import com.project.repository.MagazineRepository;
import com.project.repository.NewMagazineRequestRepo;
import com.project.repository.user.UserTxRepository;

@RestController
@RequestMapping("/pay")
@CrossOrigin
public class PayController {
	
	@Autowired
	private MagazineRepository magazineRepository;
	
	@Autowired
	private UserTxRepository userTxRepository;
	
	@Value("${server.port}")
	private String webShopClientport;
	
	@Autowired
	private FormService formService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private NewMagazineRequestRepo requestRepo;
		
	
	@RequestMapping(path = "/cart", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> cartPost(@RequestBody ShoppingCartDto request) {
		
		RestTemplate restTemplate = new RestTemplate();
		
		//izvuci tu tx iz baze 
		UserTx cart = userTxRepository.getOne(request.getCartId());
		
		//no items
		if(cart.getItems().size() == 0) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		String url = "https://localhost:8762/requestHandler/request/save";
		
		ShoppingCartRequestKpDto kpRequest = new ShoppingCartRequestKpDto(cart.getKPClientIdentifier(), cart.getTotalAmount());
		
		//ResponseEntity<OrderIdDTO> dto = restTemplate.postForEntity(url, kpRequest, OrderIdDTO.class);
		
		ResponseEntity<OrderIdDTO> dto = null;
		try {
			dto = restTemplate.exchange(url, HttpMethod.POST, createHeader(kpRequest), OrderIdDTO.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		// za izvucenu tu tx iz baze dodeliti id iz kpA
		cart.setOrderId(dto.getBody().getOrderId());
		userTxRepository.save(cart);
		
		return new ResponseEntity<OrderIdDTO>(dto.getBody(), HttpStatus.OK);
	}
	
	@RequestMapping(path = "/updateTxAfterPaymentIsFinished", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TxInfoDto> updateTxInTheEnd(@RequestBody TxInfoDto request) {		
		
		UserTx userTx = userTxRepository.findByOrderId(request.getOrderId());
		userTx.setStatus(request.getStatus());
		
		userTxRepository.save(userTx);
		
		if(userTx.getProcId() != null) {
			OrderIdDTO orderIdDto = (OrderIdDTO) runtimeService.getVariable(userTx.getProcId(), "paymentInfo");

			runtimeService.setVariable(userTx.getProcId(), "txStatus", userTx.getStatus().toString());		
//			runtimeService.setVariable(userTx.getProcId(), "txStatus", "ERROR");		

			formService.submitTaskForm(orderIdDto.getTaskId(), new HashMap<String, Object>());
		}
		
		return new ResponseEntity<TxInfoDto>(request, HttpStatus.OK);
	}
	
	@PostMapping(path = "/register")
	public ResponseEntity<?> newMagazine(@RequestBody MagazineDTO magazineDto){
		Magazine newMagazine = Magazine.builder()
								.ISSN(magazineDto.getISSN())
								.name(magazineDto.getName())
								.wayOfPayment(magazineDto.getWayOfPayment())
								.active(true)
								.membershipPrice(magazineDto.getPrice().longValue())
								.sellerIdentifier(-1l)
								.build();
		Magazine persistedMagazine = magazineRepository.save(newMagazine);
		
		String newClientRequestUrl = "https://localhost:8762/requestHandler/client/newClient/";
		
		RestTemplate restTemplate = new RestTemplate();
		// ResponseEntity<String> response = restTemplate.getForEntity(newClientRequestUrl + persistedMagazine.getMagazineId(), String.class);
		
		
		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(newClientRequestUrl + persistedMagazine.getMagazineId(), HttpMethod.GET, createHeader(null), String.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		
		return new ResponseEntity<>(new NewClientResponse(response.getBody()), HttpStatus.OK);
	}
	
	@PostMapping(path = "/registerConfirmationBasic")
	public ResponseEntity<?> newMagazineConfirmationBasic(@RequestBody NewMagazineConfirmationDto request){
		
		Magazine magazine = magazineRepository.getOne(request.getScMagazineIdentifier());
		magazine.setSellerIdentifier(request.getKpMagazineIdentifier());
		magazineRepository.save(magazine);
		
		//procesi aktiviraj
		NewMagazineRequestSession session = requestRepo.findByMagazineId(magazine.getMagazineId());
		formService.submitTaskForm(session.getTaskId(), new HashMap<String, Object>());
 
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(path = "/checkTx", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> checkTx(@RequestBody TxInfoDto request) {		
		
		UserTx tx = userTxRepository.getOne(request.getTxInfoId());
		
		request.setOrderId(tx.getOrderId());
		
		String checkUrl = "https://localhost:8762/requestHandler/request/checkTx";

		RestTemplate restTemplate = new RestTemplate();
					
		ResponseEntity<TxInfoDto> dto = null;
		try {
			dto = restTemplate.exchange(checkUrl, HttpMethod.POST, createHeader(request), TxInfoDto.class);
			
			tx.setStatus(dto.getBody().getStatus());
			userTxRepository.save(tx);
			
			return new ResponseEntity<>(dto.getBody(), HttpStatus.OK);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}
	
	private HttpEntity<?> createHeader (Object body){
		HttpHeaders headers = new HttpHeaders();
		headers.add("external", "true");
		headers.add("hostsc", "localhost:" + webShopClientport);
		
		HttpEntity<?> entity;
		if(body != null) {
			entity = new HttpEntity<>(body, headers);
		} else {
			entity = new HttpEntity<>(headers);
		}
		
		return entity;
	}

}
