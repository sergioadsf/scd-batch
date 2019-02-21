package br.com.conectasol.scdbatch.folha.service;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import br.com.conctasol.annotation.MField;
import br.com.conctasol.annotation.MIndex;
import br.com.conctasol.annotation.MSplitField;
import br.com.conectasol.scdbatch.model.Folha;
import br.com.conectasol.scdbatch.util.BulkBuilder;

@Service
public class FolhaService {
	
	private static final String PATTERN = "(([1-9]\\d{0,2}(.\\d{3})*)|(([1-9]\\d*)?\\d))(\\,\\d\\d)?$";
	
	public String toJson(Folha folha) {
			MIndex mIndex = Folha.class.getAnnotation(MIndex.class);
			String name = mIndex.name();

			return BulkBuilder
					.init(name)
					.addId(UUID.randomUUID().toString())
					.addComando("index")
					.addJson(this.prepareJson(folha))
					.build();
	}

	private String prepareJson(Folha folha) {
		try {
			StringBuilder jsonB = new StringBuilder("{");
			Field[] declaredFields = folha.getClass().getDeclaredFields();
			for (Field field : declaredFields) {
				field.setAccessible(true);

				if (field.isAnnotationPresent(MSplitField.class)) {
					MSplitField mSplitField = field.getAnnotation(MSplitField.class);
					for(MField mField : mSplitField.value()) {
						this.montarJsonMField(folha, jsonB, field, mField);
					}
				} else if (field.isAnnotationPresent(MField.class)) {
					MField mField = field.getAnnotation(MField.class);
					this.montarJsonMField(folha, jsonB, field, mField);
				}
			}
			int size = jsonB.length();
			jsonB.replace(size - 1, size, "}");

			return jsonB.toString();
		} catch (Exception e) {
			Logger.getRootLogger().error(e.getMessage(), e);
		}

		throw new IllegalAccessError();
	}

	private void montarJsonMField(Folha folha, StringBuilder jsonB, Field field, MField mField) throws IllegalAccessException {
		
		jsonB.append("\"").append(mField.name()).append("\":\"");
		Object obj = field.get(folha);
		String value = obj.toString();
		if (Stream.of("double", "float").anyMatch(p -> mField.type().equals(p))) {
//					if (obj instanceof String && ((String)obj).matches(PATTERN)) {
//						System.out.println(mField.name() +": "+mField.type() + " - " + obj.toString()+ " - "+i);
			jsonB.append(Double.valueOf(value.replaceAll(",", ".")));
		} else {
			if(mField.start() > -1) {
				jsonB.append(value.substring(mField.start(), mField.start() + mField.size()));
			}else {
				jsonB.append(value);
			}
		}
		jsonB.append("\"");

		jsonB.append(",");
	}
}
