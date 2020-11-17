package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.util.StringConverter;

public class DateUtil {

	public static StringConverter<LocalDate> formatDatePicker() {
		StringConverter<LocalDate> convert = new StringConverter<LocalDate>() {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");

			@Override
			public String toString(LocalDate object) {
				if (object != null) {
					return dateFormatter.format(object);
				} else {
					return "";
				}
			}

			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				} else {
					return null;
				}
			}
		};
		return convert;
	}

	public static Boolean isValidDate(String date) {
		final String dateFormat = "YYYY-MM-dd";
		DateFormat df = new SimpleDateFormat(dateFormat);
		df.setLenient(false);
		try {
			df.parse(date);
			return false;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return true;
		}
	}
		
}
