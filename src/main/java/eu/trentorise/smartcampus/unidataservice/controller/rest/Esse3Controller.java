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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import smartcampus.service.esse3.data.message.Esse3.Ad;
import smartcampus.service.esse3.data.message.Esse3.Cds;
import smartcampus.service.esse3.data.message.Esse3.Facolta;
import smartcampus.service.esse3.data.message.Esse3.Orari;
import smartcampus.service.esse3.data.message.Esse3.Pds;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;

import edu.emory.mathcs.backport.java.util.Collections;
import eu.trentorise.smartcampus.unidataservice.model.AdData;
import eu.trentorise.smartcampus.unidataservice.model.CdsData;
import eu.trentorise.smartcampus.unidataservice.model.FacoltaData;
import eu.trentorise.smartcampus.unidataservice.model.PdsData;
import eu.trentorise.smartcampus.unidataservice.model.TimeTableData;

@Controller
public class Esse3Controller extends RestController {

	@Autowired
	ServiceBusClient client;

	private Logger log = Logger.getLogger(this.getClass());

	@RequestMapping(method = RequestMethod.GET, value = "/getfacolta")
	public @ResponseBody
	List<FacoltaData> getFacolta(HttpServletRequest request, HttpServletResponse response) throws InvocationException {
		try {
			Map<String, Object> params = new TreeMap<String, Object>();
			return getFacolta(params);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	private List<FacoltaData> getFacolta(Map<String, Object> params) throws Exception {
		ActionInvokeParameters resp = (ActionInvokeParameters)client.invokeService("smartcampus.service.esse3", "GetFacolta", params);
		List<ByteString> bsl = resp.getDataList();
		List<FacoltaData> fsl = new ArrayList<FacoltaData>();
		for (ByteString bs : bsl) {
			Facolta facolta = Facolta.parseFrom(bs);
			fsl.add(new FacoltaData(facolta));
		}

		return fsl;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getcds/{facId}")
	public @ResponseBody
	List<CdsData> getCds(HttpServletRequest request, HttpServletResponse response, @PathVariable String facId) throws InvocationException {
		try {
			Map<String, Object> params = new TreeMap<String, Object>();
			params.put("facId", facId);
			return getCds(params);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	private List<CdsData> getCds(Map<String, Object> params) throws Exception {
		ActionInvokeParameters resp1 = (ActionInvokeParameters)client.invokeService("smartcampus.service.esse3", "GetCds", params);
		List<ByteString> bsl1 = resp1.getDataList();
		List<CdsData> fsl = new ArrayList<CdsData>();
		for (ByteString bs1 : bsl1) {
			Cds cds = Cds.parseFrom(bs1);
			fsl.add(new CdsData(cds));
		}
		return fsl;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getad/{cdsId}/{aaOrd}/{aaOff}")
	public @ResponseBody
	List<AdData> getAd(HttpServletRequest request, HttpServletResponse response, @PathVariable String cdsId, @PathVariable String aaOrd, @PathVariable String aaOff) throws InvocationException {
		try {
			Map<String, Object> params = new TreeMap<String, Object>();
			params.put("cdsId", cdsId);
			params.put("aaOrd", aaOrd);
			params.put("aaOff", aaOff);
			return getAd(params);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	private List<AdData> getAd(Map<String, Object> params) throws Exception {
		ActionInvokeParameters resp = (ActionInvokeParameters)client.invokeService("smartcampus.service.esse3", "GetAd", params);
		List<ByteString> bsl = resp.getDataList();
		List<AdData> fsl = new ArrayList<AdData>();
		for (ByteString bs : bsl) {
			Ad ad = Ad.parseFrom(bs);
			fsl.add(new AdData(ad));
		}
		return fsl;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/gettimetable/{cdscod}/{aaord}/{aaoff}/{pdsid}/{pdscod}/{adcod}/{domcod}/{fatcod}")
	public @ResponseBody
	List<TimeTableData> getTimeTable(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable String cdscod, @PathVariable String aaord, @PathVariable String aaoff, @PathVariable String pdsid, @PathVariable String pdscod, @PathVariable String adcod, @PathVariable String domcod, @PathVariable String fatcod) {
		try {

			Map<String, Object> params = new TreeMap<String, Object>();
			params.put("cdsCod", cdscod);
			params.put("aaOrd", aaord);
			params.put("aaOff", aaoff);
			params.put("pdsId", pdsid);
			params.put("pdsCod", pdscod);
			params.put("adCod", adcod);
			params.put("domCod", domcod);
			params.put("fatCod", fatcod);
			return getTimeTable(params);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	private List<TimeTableData> getTimeTable(Map<String, Object> params) throws Exception {
		ActionInvokeParameters resp = (ActionInvokeParameters)client.invokeService("smartcampus.service.esse3", "GetOrariAd", params);
		List<ByteString> bsl = resp.getDataList();
		List<TimeTableData> fsl = new ArrayList<TimeTableData>();
		for (ByteString bs : bsl) {
			Orari or = Orari.parseFrom(bs);
			fsl.add(new TimeTableData(or));
		}

		Collections.sort(fsl);
		return fsl;
	}

}
