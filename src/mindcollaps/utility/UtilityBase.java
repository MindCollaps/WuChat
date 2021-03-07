package mindcollaps.utility;

import mindcollaps.lib.Client;
import mindcollaps.lib.Message;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;

public class UtilityBase {

    public static JSONObject convertStringToJson(String json) {
        JSONObject object = null;
        JSONParser parser = new JSONParser();
        try {
            object = (JSONObject) parser.parse(json);
        } catch (ParseException ignored) {
        }
        return object;
    }

    public static String convertJsonToString(JSONObject object) {
        return object.toJSONString();
    }

    public static Message convertJsonToMessage(JSONObject jsMessage){
        Message message = new Message();
        message.setMessageId((String) jsMessage.get("id"));
        message.setContent((String) jsMessage.get("content"));
        message.setMessageType(Message.MessageType.valueOf((String) jsMessage.get("type")));
        message.setFromDate(Date.from(Instant.parse((CharSequence) jsMessage.get("date"))));
        message.setFromClient(convertJsonToClient((JSONObject) jsMessage.get("from")));
        return message;
    }

    public static Client convertJsonToClient(JSONObject jsClient){
        Client client = new Client();
        client.setClientId((String) jsClient.get("id"));
        client.setClientDisplayName((String) jsClient.get("name"));
        return client;
    }

    public static String generateRandomToken(int length){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }
}
