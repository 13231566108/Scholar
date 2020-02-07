package c.jt.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemDesc;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class ObjectToJson {
	private static final ObjectMapper MAPPER = new ObjectMapper();

	// 对象转换成json
	@Test
	public void toJson() throws JsonProcessingException {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(1L).setItemDesc("ss").setCreated(new Date());
		String json = MAPPER.writeValueAsString(itemDesc);
		System.out.println(json);

		ItemDesc itemDesc1 = MAPPER.readValue(json,ItemDesc.class);
		System.out.println(itemDesc1.getItemId());
	}
}
