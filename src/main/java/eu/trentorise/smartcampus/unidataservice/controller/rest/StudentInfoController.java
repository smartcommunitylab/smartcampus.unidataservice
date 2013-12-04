/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.unidataservice.controller.rest;

import it.sayservice.platform.client.InvocationException;
import it.sayservice.platform.client.ServiceBusClient;
import it.sayservice.platform.core.message.Core.ActionInvokeParameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import smartcampus.service.esse3.data.message.Esse3.Student;
import smartcampus.service.esse3.data.message.Esse3.StudentExams;
import smartcampus.services.dbconnector.opera.data.message.Operadb.DBOperaStudent;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.ProfileServiceException;
import eu.trentorise.smartcampus.profileservice.model.AccountProfile;
import eu.trentorise.smartcampus.social.model.User;
import eu.trentorise.smartcampus.unidataservice.model.OperaStudent;
import eu.trentorise.smartcampus.unidataservice.model.StudentInfoData;
import eu.trentorise.smartcampus.unidataservice.model.StudentInfoExam;
import eu.trentorise.smartcampus.unidataservice.model.StudentInfoExams;

@Controller
public class StudentInfoController extends RestController {

	private static final String AUTH_TOKEN = "OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE";

	@Autowired
	ServiceBusClient client;

	@Value("${basic.profile.server.url}")
	private String profileURL;

	private Logger log = Logger.getLogger(this.getClass());

	// STUDENT PERSONAL DATA FROM ADA

	@RequestMapping(method = RequestMethod.GET, value = "/getstudentdata")
	public @ResponseBody
	StudentInfoData getStudentInfo(HttpServletRequest request, HttpServletResponse response) throws InvocationException {
		try {
			User user = getCurrentUser();
			String userId = getUserId(user);
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}

			String token = getToken(request);
			String idAda = getIdAda(token);
			StudentInfoData sd = getStudentInfo(idAda);
			if (sd != null) {
				return sd;
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}

			return null;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getstudentdata/{userId}")
	public @ResponseBody
	StudentInfoData getStudentInfo(HttpServletRequest request, HttpServletResponse response, @PathVariable String userId) throws InvocationException {
		try {
			User user = getUserObject(userId);
			if (user == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}

			String token = getToken(request);
			String idAda = getIdAda(userId, token);
			StudentInfoData sd = getStudentInfo(idAda);
			if (sd != null) {
				return sd;
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}

			return null;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	private StudentInfoData getStudentInfo(String idAda) throws Exception {
		if (idAda == null) {
			return null;
		}
		Map<String, Object> pars = new TreeMap<String, Object>();
		pars.put("idAda", idAda);
		ActionInvokeParameters resp = (ActionInvokeParameters)client.invokeService("smartcampus.service.esse3", "GetStudentData", pars);

		if (resp.getDataCount() == 1) {
			Student student = Student.parseFrom(resp.getData(0));
			StudentInfoData sd = new StudentInfoData(student);
			return sd;
		} else {
			return null;
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "/getstudentexams")
	public @ResponseBody
	StudentInfoExams getStudentExams(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws InvocationException {
		try {
			User user = getCurrentUser();
			String userId = getUserId(user);
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}

			String token = getToken(request);
			String idAda = getIdAda(token);
			StudentInfoExams result = getStudentExams(idAda);
			if (result != null) {
				return result;
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getstudentexams/{userId}")
	public @ResponseBody
	StudentInfoExams getStudentExams(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable String userId) throws InvocationException {
		try {
			User user = getUserObject(userId);
			if (user == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}

			String token = getToken(request);
			String idAda = getIdAda(userId, token);
			StudentInfoExams result = getStudentExams(idAda);
			if (result != null) {
				return result;
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	private StudentInfoExams getStudentExams(String idAda) throws Exception {
		if (idAda == null) {
			return null;
		}
		Map<String, Object> pars = new TreeMap<String, Object>();
		pars.put("idAda", idAda);
		ActionInvokeParameters resp = (ActionInvokeParameters)client.invokeService("smartcampus.service.esse3", "GetStudentExams", pars);

		List<StudentInfoExam> sie = new ArrayList<StudentInfoExam>();
		if (resp.getDataCount() == 1) {
			StudentExams exams = StudentExams.parseFrom(resp.getData(0));
			StudentInfoExams result = new StudentInfoExams(exams);
			return result;
		} else {
			return null;
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getoperacard")
	public @ResponseBody
	OperaStudent getCard(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		try {
			User user = getCurrentUser();
			String userId = getUserId(user);
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}

			String token = getToken(request);
			String idAda = getIdAda(token);
			OperaStudent result = getCard(idAda);
			if (result != null) {
				return result;
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getoperacard/{userId}")
	public @ResponseBody
	OperaStudent getCard(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable String userId) throws IOException {
		try {
			User user = getUserObject(userId);
			if (user == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}

			String token = getToken(request);
			String idAda = getIdAda(userId, token);
			OperaStudent result = getCard(idAda);
			if (result != null) {
				return result;
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	private OperaStudent getCard(String idAda) throws Exception {
		Student student = getStudent(idAda);
		if (student == null) {
			return null;
		}
		String idGiada = student.getIdGiada();

		Map<String, Object> pars = new TreeMap<String, Object>();
		pars.put("idGiada", idGiada);
		ActionInvokeParameters resp = (ActionInvokeParameters)client.invokeService("smartcampus.services.dbconnector.opera", "GetOperaPayments", pars);

		List<StudentInfoExam> sie = new ArrayList<StudentInfoExam>();
		if (resp.getDataCount() == 1) {
			DBOperaStudent operaStudent = DBOperaStudent.parseFrom(resp.getData(0));
			OperaStudent result;
			if (operaStudent.hasUidbadge()) {
				result = new OperaStudent(operaStudent);
			} else {
				result = new OperaStudent(student);
			}
			return result;
		} else {
			return null;
		}
	}

	private String getToken(HttpServletRequest request) {
		return (String) request.getAttribute(AUTH_TOKEN);
	}

	protected User getCurrentUser() {
		String uid = this.getUser().getUsername();
		User user = getUserObject(uid);
		return user;
	}

	protected String getUserId(User user) {
		return user.getId();
	}

	protected String getIdAda(String token) throws SecurityException, ProfileServiceException {
		BasicProfileService bps = new BasicProfileService(profileURL);
		AccountProfile profile = bps.getAccountProfile(token);
		Map<String, String> attrs = profile.getAccountAttributes("unitn");
		if (attrs != null && attrs.containsKey("idada")) {
			return attrs.get("idada");
		} else {
			return null;
		}
	}

	protected String getIdAda(String userId, String token) throws SecurityException, ProfileServiceException {
		BasicProfileService bps = new BasicProfileService(profileURL);
		List<String> id = Arrays.asList(userId);
		List<AccountProfile> profiles = bps.getAccountProfilesByUserId(id, token);
		if (profiles != null && profiles.size() == 1) {
			Map<String, String> attrs = profiles.get(0).getAccountAttributes("unitn");
			if (attrs != null && attrs.containsKey("idada")) {
				return attrs.get("idada");
			}
		}
		return null;

	}

	protected Student getStudent(String idAda) throws Exception {
		if (idAda == null) {
			return null;
		}
		Map<String, Object> pars = new TreeMap<String, Object>();
		pars.put("idAda", idAda);
		ActionInvokeParameters resp = (ActionInvokeParameters)client.invokeService("smartcampus.service.esse3", "GetStudentData", pars);

		if (resp.getDataCount() == 1) {
			Student student = Student.parseFrom(resp.getData(0));
			return student;
		} else {
			return null;
		}
	}

}
