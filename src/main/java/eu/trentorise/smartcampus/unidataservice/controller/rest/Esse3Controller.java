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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

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
import smartcampus.service.esse3.data.message.Esse3.CalendarCds;
import smartcampus.service.esse3.data.message.Esse3.Cds;
import smartcampus.service.esse3.data.message.Esse3.Facolta;
import smartcampus.service.esse3.data.message.Esse3.Orari;

import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Collections2;
import com.google.protobuf.ByteString;

import edu.emory.mathcs.backport.java.util.Collections;
import eu.trentorise.smartcampus.unidataservice.model.AdData;
import eu.trentorise.smartcampus.unidataservice.model.CalendarCdsData;
import eu.trentorise.smartcampus.unidataservice.model.CdsData;
import eu.trentorise.smartcampus.unidataservice.model.FacoltaData;
import eu.trentorise.smartcampus.unidataservice.model.TimeTableData;

@Controller
public class Esse3Controller extends RestController {

	@Autowired
	ServiceBusClient client;

	private Logger log = Logger.getLogger(this.getClass());

	private LoadingCache<String, List<CalendarCdsData>> cdsCache;
	private LoadingCache<String, List<CalendarCdsData>> adCache;

	private long CACHE_TIME = 24;

	public Esse3Controller() {
		cdsCache = CacheBuilder.newBuilder().expireAfterWrite(CACHE_TIME, TimeUnit.HOURS).build(new CacheLoader<String, List<CalendarCdsData>>() {
			@Override
			public List<CalendarCdsData> load(String key) throws Exception {
				System.err.println("Loading cds " + key);
				Map<String, Object> params = new TreeMap<String, Object>();
				String pars[] = key.split("_");
				params.put("cdsId", pars[0]);
				params.put("anno", pars[1]);
				return getFullCdsCalendar(params);
			}
		});
		adCache = CacheBuilder.newBuilder().expireAfterWrite(CACHE_TIME, TimeUnit.HOURS).build(new CacheLoader<String, List<CalendarCdsData>>() {
			@Override
			public List<CalendarCdsData> load(String key) throws Exception {
				System.err.println("Loading ad " + key);
				Map<String, Object> params = new TreeMap<String, Object>();
				params.put("adId", key);
				return getFullAdCalendar(params);
			}
		});
	}

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
		ActionInvokeParameters resp = (ActionInvokeParameters) client.invokeService("smartcampus.service.esse3", "GetFacolta", params);
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
		ActionInvokeParameters resp1 = (ActionInvokeParameters) client.invokeService("smartcampus.service.esse3", "GetCds", params);
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
		ActionInvokeParameters resp = (ActionInvokeParameters) client.invokeService("smartcampus.service.esse3", "GetAd", params);
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
		ActionInvokeParameters resp = (ActionInvokeParameters) client.invokeService("smartcampus.service.esse3", "GetOrariAd", params);
		List<ByteString> bsl = resp.getDataList();
		List<TimeTableData> fsl = new ArrayList<TimeTableData>();
		for (ByteString bs : bsl) {
			Orari or = Orari.parseFrom(bs);
			fsl.add(new TimeTableData(or));
		}

		Collections.sort(fsl);
		return fsl;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getcdscalendar/{cdsid}/{year}")
	public @ResponseBody
	List<CalendarCdsData> getCdsCalendar(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable String cdsid, @PathVariable String year) {
		try {

			Map<String, Object> params = new TreeMap<String, Object>();
			params.put("cdsId", cdsid);
			params.put("anno", year);
			return getCdsCalendar(params);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	private List<CalendarCdsData> getCdsCalendar(Map<String, Object> params) throws Exception {
		ActionInvokeParameters resp = (ActionInvokeParameters) client.invokeService("smartcampus.service.esse3", "GetCalendarioCds", params);
		List<ByteString> bsl = resp.getDataList();
		List<CalendarCdsData> cc = new ArrayList<CalendarCdsData>();
		for (ByteString bs : bsl) {
			CalendarCds or = CalendarCds.parseFrom(bs);
			cc.add(new CalendarCdsData(or));
		}

		return cc;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getfullcdscalendar/{cdsid}/{year}")
	public @ResponseBody
	List<CalendarCdsData> getFullCdsCalendar(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable String cdsid, @PathVariable String year) {
		try {
			List<CalendarCdsData> result = cdsCache.get(cdsid + "_" + year);
			result = filterCalendarByDate(result, null, null);
			return result;

			// Map<String, Object> params = new TreeMap<String, Object>();
			// params.put("cdsId", cdsid);
			// params.put("anno", year);
			// return getCompleteCdsCalendar(params);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getfullcdscalendar/{cdsid}/{year}/{from}/{to}")
	public @ResponseBody
	List<CalendarCdsData> getFullCdsCalendar(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable String cdsid, @PathVariable String year, @PathVariable Long from, @PathVariable Long to) {
		try {
			List<CalendarCdsData> result = cdsCache.get(cdsid + "_" + year);
			result = filterCalendarByDate(result, from, to);
			return result;

			// Map<String, Object> params = new TreeMap<String, Object>();
			// params.put("cdsId", cdsid);
			// params.put("anno", year);
			// return getCompleteCdsCalendar(params);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	private List<CalendarCdsData> getFullCdsCalendar(Map<String, Object> params) throws Exception {
		ActionInvokeParameters resp = (ActionInvokeParameters) client.invokeService("smartcampus.service.esse3", "GetCalendarioCompletoCds", params);
		List<ByteString> bsl = resp.getDataList();
		List<CalendarCdsData> cc = new ArrayList<CalendarCdsData>();
		for (ByteString bs : bsl) {
			CalendarCds or = CalendarCds.parseFrom(bs);
			cc.add(new CalendarCdsData(or));
		}

		return cc;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getfulladcalendar/{adid}")
	public @ResponseBody
	List<CalendarCdsData> getFullAdCalendar(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable String adid) {
		try {
			List<CalendarCdsData> result = adCache.get(adid);
			result = filterCalendarByDate(result, null, null);
			return result;

			// Map<String, Object> params = new TreeMap<String, Object>();
			// params.put("adId", adid);
			// return getCompleteAdCalendar(params);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getfulladcalendar/{adid}/{from}/{to}")
	public @ResponseBody
	List<CalendarCdsData> getFullAdCalendar(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable String adid, @PathVariable Long from, @PathVariable Long to) {
		try {
			List<CalendarCdsData> result = adCache.get(adid);
			result = filterCalendarByDate(result, from, to);
			return result;

			// Map<String, Object> params = new TreeMap<String, Object>();
			// params.put("adId", adid);
			// return getCompleteAdCalendar(params);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	private List<CalendarCdsData> getFullAdCalendar(Map<String, Object> params) throws Exception {
		ActionInvokeParameters resp = (ActionInvokeParameters) client.invokeService("smartcampus.service.esse3", "GetCalendarioCompletoAd", params);
		List<ByteString> bsl = resp.getDataList();
		List<CalendarCdsData> cc = new ArrayList<CalendarCdsData>();
		for (ByteString bs : bsl) {
			CalendarCds or = CalendarCds.parseFrom(bs);
			cc.add(new CalendarCdsData(or));
		}

		return cc;
	}

	private List<CalendarCdsData> filterCalendarByDate(List<CalendarCdsData> data, final Long from, final Long to) {

		return new ArrayList<CalendarCdsData>(Collections2.filter(data, new Predicate<CalendarCdsData>() {

			@Override
			public boolean apply(CalendarCdsData cd) {
				if (from == null || to == null) {
					return true;
					// return cd.getFrom() > System.currentTimeMillis();
				} else {
					return cd.getFrom() > from && cd.getFrom() < to;
				}
			}

		}));
		// long time = System.currentTimeMillis();

	}

}
