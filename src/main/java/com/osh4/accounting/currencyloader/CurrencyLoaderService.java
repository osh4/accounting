package com.osh4.accounting.currencyloader;

import java.time.LocalDate;

import javax.xml.soap.SOAPException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * @author osh4 <kosntantin@osh4.com>
 */
public interface CurrencyLoaderService
{
	double getCurrencyFactorByDateAndIso(String iso, LocalDate date) throws TransformerException, SOAPException;
}
