package org.activiti.standalone.parsing;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.impl.test.ResourceActivitiTestCase;
import org.activiti.engine.impl.util.io.InputStreamSource;
import org.activiti.engine.impl.util.io.StreamSource;
import org.activiti.engine.repository.Deployment;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class ChineseConverterTest extends ResourceActivitiTestCase {

	public ChineseConverterTest() {
		super("org/activiti/standalone/parsing/encoding.activiti.cfg.xml");
	}

	public void testConvertXMLToModel() throws Exception {
		System.out.println( "CHARSET "+Charset.defaultCharset());
		BpmnModel bpmnModel = readXMLFile();
		bpmnModel = exportAndReadXMLFile(bpmnModel);
		deployProcess(bpmnModel);
	}

	protected String getResource() {
		return "org/activiti/standalone/parsing/chinese.bpmn";
	}

	protected BpmnModel readXMLFile() throws Exception {
		InputStream xmlStream = this.getClass().getClassLoader().getResourceAsStream(getResource());
		StreamSource xmlSource = new InputStreamSource(xmlStream);
		BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xmlSource, false, false, processEngineConfiguration.getXmlEncoding());
		return bpmnModel;
	}

	protected BpmnModel exportAndReadXMLFile(BpmnModel bpmnModel) throws Exception {
		byte[] xml = new BpmnXMLConverter().convertToXML(bpmnModel, processEngineConfiguration.getXmlEncoding());
		System.out.println("xml " + new String(xml, processEngineConfiguration.getXmlEncoding()));
		StreamSource xmlSource = new InputStreamSource(new ByteArrayInputStream(xml));
		BpmnModel parsedModel = new BpmnXMLConverter().convertToBpmnModel(xmlSource, false, false, processEngineConfiguration.getXmlEncoding());
		return parsedModel;
	}

	protected void deployProcess(BpmnModel bpmnModel) throws UnsupportedEncodingException {
		byte[] xml = new BpmnXMLConverter().convertToXML(bpmnModel);
		try {
			Deployment deployment = processEngine.getRepositoryService().createDeployment().name("test").addString("test.bpmn20.xml", new String(xml, "UTF-8")).deploy();
			processEngine.getRepositoryService().deleteDeployment(deployment.getId());
		} finally {
			processEngine.close();
		}
	}
}
