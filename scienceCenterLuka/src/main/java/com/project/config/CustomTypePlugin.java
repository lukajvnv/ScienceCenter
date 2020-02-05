package com.project.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.form.type.AbstractFormFieldType;

import com.project.util.camunda.customType.FileFormType;
import com.project.util.camunda.customType.MultiEnumFormType;
import com.project.util.camunda.customType.MultiEnumFormTypeCoAuthor;
import com.project.util.camunda.customType.MultiEnumFormTypeCoAuthorOut;
import com.project.util.camunda.customType.MultiEnumFormTypeTermOut;

//@Component
public class CustomTypePlugin extends AbstractProcessEnginePlugin {

	@Override
	public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
		// TODO Auto-generated method stub
		//super.preInit(processEngineConfiguration);
		if (processEngineConfiguration.getCustomFormTypes() == null) {
            processEngineConfiguration.setCustomFormTypes(new ArrayList<AbstractFormFieldType>());
		}
		
		List<AbstractFormFieldType> formTypes = processEngineConfiguration.getCustomFormTypes();
        formTypes.add(new MultiEnumFormType(new HashMap<String, String>()));
        formTypes.add(new MultiEnumFormTypeCoAuthor(new HashMap<String, String>()));
        formTypes.add(new MultiEnumFormTypeCoAuthorOut());
        formTypes.add(new MultiEnumFormTypeTermOut(new HashMap<String, String>()));
        formTypes.add(new FileFormType());
	}

}
