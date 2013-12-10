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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.QueryBuilder;

import eu.trentorise.smartcampus.unidataservice.model.Menu;

@Controller
public class CanteenController extends RestController {

	@Autowired
	private MongoTemplate mongoTemplate;

	private Logger log = Logger.getLogger(this.getClass());

	/**
	 * 
	 * @param request
	 * @param response
	 * @param from start date in format yyyy-mm-dd
	 * @param to end date in format yyyy-mm-dd
	 * @return
	 * @throws InvocationException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/data/getmenu/{from}/{to}")
	public @ResponseBody
	List<Menu> getMenu(HttpServletRequest request, HttpServletResponse response, @PathVariable String from, @PathVariable String to) throws InvocationException {
		try {
			Criteria criteria = new Criteria("date").gte(from).lte(to);
			Query query = new Query(criteria);
			
			List<Menu> result = mongoTemplate.find(query, Menu.class, "menu");
			
			return result;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}	

}
