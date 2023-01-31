package com.increff.posapp.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DoubleUtil {
	public static Double round(Double value, Integer places) throws NumberFormatException{
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static String roundToString(Double num) throws NumberFormatException{
		return String.format("%.2f", num);
	}
}
