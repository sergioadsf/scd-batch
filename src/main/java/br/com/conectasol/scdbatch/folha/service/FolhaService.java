package br.com.conectasol.scdbatch.folha.service;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import br.com.conctasol.annotation.MField;
import br.com.conctasol.annotation.MIndex;
import br.com.conectasol.scdbatch.model.Folha;
import br.com.conectasol.scdbatch.util.BulkBuilder;

@Service
public class FolhaService {
	
	private static final String PATTERN = "(([1-9]\\d{0,2}(.\\d{3})*)|(([1-9]\\d*)?\\d))(\\,\\d\\d)?$";
	
	static int i = 0; 
	
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
		i++;
		try {
			StringBuilder jsonB = new StringBuilder("{");
			Field[] declaredFields = folha.getClass().getDeclaredFields();
			for (Field field : declaredFields) {
				if (field.isAnnotationPresent(MField.class)) {
					field.setAccessible(true);
					MField mField = field.getAnnotation(MField.class);
					jsonB.append("\"").append(mField.name()).append("\":\"");
					Object obj = field.get(folha);
					if (Stream.of("double", "float").anyMatch(p -> mField.type().equals(p))) {
//					if (obj instanceof String && ((String)obj).matches(PATTERN)) {
//						System.out.println(mField.name() +": "+mField.type() + " - " + obj.toString()+ " - "+i);
						jsonB.append(Double.valueOf(obj.toString().replaceAll(",", ".")));
					} else {
						jsonB.append(obj);
					}
					jsonB.append("\"");

					jsonB.append(",");
				}
			}
			int size = jsonB.length();
			jsonB.replace(size - 1, size, "}");

			return jsonB.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		throw new IllegalAccessError();
	}
}
