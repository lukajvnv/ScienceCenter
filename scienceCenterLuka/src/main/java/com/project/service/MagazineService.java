package com.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.model.Magazine;
import com.project.repository.UnityOfWork;

@Service
public class MagazineService {

	@Autowired
	private UnityOfWork unityOfWork;
	
	public List<Magazine> getMagazines(){
		return unityOfWork.getMagazineRepository().findAll().stream().filter(m -> m.isActive()).collect(Collectors.toList());
	}
	
	public Magazine getMagazine(Long id) {
		return unityOfWork.getMagazineRepository().getOne(id);
	}
}