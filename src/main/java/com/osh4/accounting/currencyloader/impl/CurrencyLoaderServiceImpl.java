package com.osh4.accounting.currencyloader.impl;

import java.time.LocalDate;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.springframework.stereotype.Service;

import com.osh4.accounting.currencyloader.CurrencyLoaderService;

/**
 * @author osh4 <kosntantin@osh4.com>
 */
@Service
public class CurrencyLoaderServiceImpl implements CurrencyLoaderService
{
	@Override
	public double getCurrencyFactorByDateAndIso(String iso, LocalDate date) throws TransformerException, SOAPException
	{
		SOAPMessage soapMessage = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();
		SOAPPart part = soapMessage.getSOAPPart();
		SOAPEnvelope envelope = part.getEnvelope();
		SOAPBody body = envelope.getBody();
		Name bodyName = envelope.createName("AllDataInfoXML", null, "http://web.cbr.ru/");
		body.addBodyElement(bodyName);
		soapMessage.saveChanges();
		System.out.println(soapMessage);

		//Отправка сообщения и получение ответа
		//Установка адресата
		String destination = "http://www.cbr.ru/DailyInfoWebServ/DailyInfo.asmx";
		//Отправка
		SOAPConnectionFactory soapConnFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection connection = soapConnFactory.createConnection();
		SOAPMessage reply = connection.call(soapMessage, destination);

		//ответ
		//Проверка полученного ответа
		System.out.println("\nRESPONSE:\n");
		//Создание XSLT-процессора
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		//Получение содержимого ответа
		Source sourceContent = reply.getSOAPPart().getContent();
		//Задание выходного потока для результата преобразования
		StreamResult result = new StreamResult(System.out);
		transformer.transform(sourceContent, result);

		return 0;
	}
}
