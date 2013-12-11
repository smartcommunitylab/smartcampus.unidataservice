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
package eu.trentorise.smartcampus.unidataservice.processor;

import it.sayservice.platform.client.ServiceBusListener;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import smartcampus.services.dbconnector.opera.data.message.Operadb.DBDish;
import smartcampus.services.dbconnector.opera.data.message.Operadb.DataMenu;
import smartcampus.services.dbconnector.opera.data.message.Operadb.DataOpening;
import smartcampus.services.dbconnector.opera.data.message.Operadb.DataOpeningCanteen;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import edu.emory.mathcs.backport.java.util.Collections;
import eu.trentorise.smartcampus.unidataservice.listener.Subscriber;
import eu.trentorise.smartcampus.unidataservice.model.CanteenOpening;
import eu.trentorise.smartcampus.unidataservice.model.CanteenOpeningTimes;
import eu.trentorise.smartcampus.unidataservice.model.Dish;
import eu.trentorise.smartcampus.unidataservice.model.Menu;

public class EventProcessorImpl implements ServiceBusListener {

	@Autowired
	private MongoTemplate mongoTemplate;

	private static Log logger = LogFactory.getLog(EventProcessorImpl.class);

	@Override
	public void onServiceEvents(String serviceId, String methodName, String subscriptionId, List<ByteString> data) {
		try {
			if (Subscriber.OPERA.equals(serviceId)) {
				if (Subscriber.GET_MENU.equals(methodName)) {
					updateMenu(data);
				} else if (Subscriber.GET_APERTURE.equals(methodName)) {
					updateAperture(data);
				}
			}
		} catch (Exception e) {
			logger.error("Error updating " + methodName);
			e.printStackTrace();
		}
	}

	private void updateMenu(List<ByteString> data) throws InvalidProtocolBufferException {
		for (ByteString bs : data) {
			DataMenu dm = DataMenu.parseFrom(bs);
			Menu menu = new Menu();

			menu.setDate(dm.getDate());
			menu.setType(dm.getType());
			menu.setId(encode(dm.getDate() + "_" + dm.getType()));

			for (DBDish dd : dm.getDishesList()) {
				Dish dish = new Dish();
				dish.setCal(dd.getCal());
				dish.setName(dd.getNome());

				menu.getDishes().add(dish);

			}

			mongoTemplate.save(menu, "menu");
		}
	}

	private void updateAperture(List<ByteString> data) throws InvalidProtocolBufferException {
		for (ByteString bs : data) {
			DataOpening da = DataOpening.parseFrom(bs);

			CanteenOpening opening = new CanteenOpening();
			opening.setCanteen(da.getDescrzona());
			opening.setId(encode(da.getDescrzona()));

			Map<String, CanteenOpeningTimes> zones = new TreeMap<String, CanteenOpeningTimes>();
			for (DataOpeningCanteen dac : da.getOpeningsList()) {
				CanteenOpeningTimes times;
				if (zones.containsKey(dac.getDescrmensa())) {
					times = zones.get(dac.getDescrmensa());
				} else {
					times = new CanteenOpeningTimes();
					zones.put(dac.getDescrmensa(), times);
				}

				times.setType(dac.getDescrmensa());

				for (String d : dac.getDateList()) {
					times.getDates().add(d);
				}

				Collections.sort(times.getDates());
				if (!opening.getTimes().contains(times)) {
					opening.getTimes().add(times);
				}

			}

			mongoTemplate.save(opening, "opening");
		}

	}

	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	private static String encode(String s) {
		return new BigInteger(s.getBytes()).toString(16);
	}

}