package com.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.repository.UnityOfWork;

@Service
public class UserService {

	@Autowired
	private UnityOfWork unityOfWork;
}
