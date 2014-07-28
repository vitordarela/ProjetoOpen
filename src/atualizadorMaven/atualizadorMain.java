package atualizadorMaven;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXModifier;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class atualizadorMain {

	public static void main(String[] args) throws DocumentException, IOException  {
		String fileRaiz;
		String filePrincipal = null;
		String fileWeb = null;
		String fileEntity = null;
		String fileCommon = null;
		String fileEAP = null;
		
	    String nome = "caminho.txt";
	    
	    try {
	      FileReader arq = new FileReader(nome);
	      BufferedReader lerArq = new BufferedReader(arq);

	      String linha = lerArq.readLine(); // lê a primeira linha
	      // a variável "linha" recebe o valor "null" quando o processo
	      // de repetição atingir o final do arquivo texto
	      while (linha != null) {
	    	fileRaiz = linha.replace("\\", "/");
//	  		filePrincipal = fileRaiz+"/sgn2/pom.xml";
//			fileWeb = fileRaiz+"/sgn2/sgn2-web/pom.xml";
//			fileEntity = fileRaiz+"/entities/senai-entity/pom.xml";
//			fileCommon = fileRaiz+"/jlibrary/senai-common/pom.xml";
//			fileEAP = fileRaiz+"/jlibrary/senai-lib/pom.xml";
			
	    	//Caminho apenas para Vitor
	  		filePrincipal = fileRaiz+"/sgn2/pom.xml";
			fileWeb = fileRaiz+"/sgn2/sgn2-web/pom.xml";
			fileEntity = fileRaiz+"/senai-entity/pom.xml";
			fileCommon = fileRaiz+"/senai-common/pom.xml";
			fileEAP = fileRaiz+"/senai-lib/pom.xml";
			
			linha = lerArq.readLine();
	      }

	      arq.close();
	    } catch (IOException e) {
			fileRaiz = JOptionPane.showInputDialog("Caminho da Pasta onde estão os projetos: \n\n Ex: C:/SENAI/Fontes/java/aplicacoes/sgn2/NTI/tarefas  \n Obs: Colocar igual ao exemplo, não colocar '/' na última pasta").replace("\\", "/");
//			filePrincipal = fileRaiz+"/sgn2/pom.xml";
//			fileWeb = fileRaiz+"/sgn2/sgn2-web/pom.xml";
//			fileEntity = fileRaiz+"/entities/senai-entity/pom.xml";
//			fileCommon = fileRaiz+"/jlibrary/senai-common/pom.xml";
//			fileEAP = fileRaiz+"/jlibrary/senai-lib/pom.xml";
			
			//Caminho apenas para Vitor
	  		filePrincipal = fileRaiz+"/sgn2/pom.xml";
			fileWeb = fileRaiz+"/sgn2/sgn2-web/pom.xml";
			fileEntity = fileRaiz+"/senai-entity/pom.xml";
			fileCommon = fileRaiz+"/senai-common/pom.xml";
			fileEAP = fileRaiz+"/senai-lib/pom.xml";
	    }

		/**
		 * Utilizado apenas para testes.
		 */
//		filePrincipal = "C:/SENAI/Fontes/java/aplicacoes/sgn2/NTI/tarefas/atualizadorMaven/src/META-INF/pom.xml";
//		String fileWeb = "C:/SENAI/Fontes/java/aplicacoes/sgn2/NTI/tarefas/atualizadorMaven/src/META-INF/pom-web.xml";
//		String fileEntity = "C:/SENAI/Fontes/java/aplicacoes/sgn2/NTI/tarefas/atualizadorMaven/src/META-INF/pom-entity.xml";
//		String fileCommon = "C:/SENAI/Fontes/java/aplicacoes/sgn2/NTI/tarefas/atualizadorMaven/src/META-INF/pom-common.xml";
//		String fileEAP = "C:/SENAI/Fontes/java/aplicacoes/sgn2/NTI/tarefas/atualizadorMaven/src/META-INF/pom-lib.xml";
		
					
			SAXModifier reader = new SAXModifier();
			File filename = new File(filePrincipal);
			Document doc = reader.modify(filename);
			 
			Element root = doc.getRootElement();
			StringBuffer sb = new StringBuffer();
			
			for ( Object obj : root.elements()) {
			    Element urlElement = (Element) obj;
			    sb.setLength(0); 
			    sb.append(urlElement.elementText("senai.bom.eap.version"));
			    sb.append(urlElement.elementText("senai.commons.version"));
			    sb.append(urlElement.elementText("senai.entity.version"));
			    if((urlElement.element("senai.bom.eap.version") != null) && (urlElement.element("senai.commons.version")  != null) && (urlElement.element("senai.entity.version")  != null)) {
			    	System.out.println("POM SGN2: "+root.elementText("version"));
			    	
			        // lê o documento novamente para alterar
			        XMLWriter writer = new XMLWriter(new FileWriter(filePrincipal));
			       
			        root.element("version").setText(pegaPomWeb(fileWeb));
			        urlElement.element("senai.entity.version").setText(pegaPomEntity(fileEntity, fileEAP, fileCommon));
			        urlElement.element("senai.commons.version").setText(pegaPomCommon(fileCommon,fileEAP));
			        urlElement.element("senai.bom.eap.version").setText(pegaPomEAP(fileEAP));
			        writer.write(doc);
			        writer.close();


			        // imprimi novamente o XML
			        OutputFormat format = OutputFormat.createPrettyPrint();
			        writer = new XMLWriter( System.out, format );
			        writer.write( doc );

			        // Compacta o XML
			        format = OutputFormat.createCompactFormat();
			        writer = new XMLWriter( System.out, format );
			        writer.write( doc );
			    }
			}
			JOptionPane.showMessageDialog(null, "POM atualizado com êxito!! \n\n Pom SGN2 - "+root.elementText("version")+"\n Pom Entity -"+pegaPomEntity(fileEntity, fileEAP, fileCommon) +"\n Pom Common - "+pegaPomCommon(fileCommon, fileEAP)+"\n Pom EAP - "+pegaPomEAP(fileEAP));
	}
			

	public static String pegaPomWeb(String fileWeb) throws DocumentException {
		
		SAXReader reader = new SAXReader();
		File filename = new File(fileWeb);
		Document doc = reader.read(filename);
		 
		Element root = doc.getRootElement();
		StringBuffer sb = new StringBuffer();
		for ( Object obj : root.elements()) {
		    Element urlElement = (Element) obj;
		    sb.setLength(0);
		    sb.append(urlElement.elementText("version"));
		    if(urlElement.element("version") != null) {
		    	String web = urlElement.element("version").getText(); 
		    	return web;

			}
		}
		return null;
	}
	
	public static String pegaPomEntity(String fileEntity, String fileEAP, String fileCommon) throws DocumentException, IOException {
		
		SAXModifier reader = new SAXModifier();
		File filename = new File(fileEntity);
		Document doc = reader.modify(filename);
		Element root = doc.getRootElement();
		
		StringBuffer sb = new StringBuffer();
		    if(root.element("version").getText() != null) {
		    	String entity = root.element("version").getText(); 

		    	for ( Object obj : root.elements()) {
				    Element urlElement = (Element) obj;
				    sb.setLength(0); 
				    if(urlElement.element("senai.bom.eap.version") != null && urlElement.elementText("senai.commons.version") != null) {
				    	// lê o documento novamente para alterar
				    	XMLWriter writer = new XMLWriter(new FileWriter(fileEntity));

				    	urlElement.element("senai.bom.eap.version").setText(pegaPomEAP(fileEAP));
				    	urlElement.element("senai.commons.version").setText(pegaPomCommon(fileCommon, fileEAP));
				    	String alteradorEAP = urlElement.element("senai.bom.eap.version").getText();
				    	String alteradorCommon = urlElement.element("senai.commons.version").getText();
				    	System.out.println(alteradorEAP);
				    	System.out.println(alteradorCommon);

				    	writer.write(doc);
				    	writer.close();


						// imprimi novamente o XML
						OutputFormat format = OutputFormat.createPrettyPrint();
						writer = new XMLWriter(System.out, format);
						writer.write(doc);
	
						// Compacta o XML
						format = OutputFormat.createCompactFormat();
						writer = new XMLWriter(System.out, format);
						writer.write(doc);
						break;

				    }
		    	}
		    	return entity;
		    }
		    
		return null;
	}
	
	public static String pegaPomCommon(String fileCommon, String FileEAP) throws DocumentException, IOException {
		
		SAXReader reader = new SAXReader();
		File filename = new File(fileCommon);
		Document doc = reader.read(filename);
		 
		Element root = doc.getRootElement();
		    if(root.element("version").getText() != null) {
		    	String common = root.element("version").getText(); 
		    	StringBuffer sb = new StringBuffer();

			    	for ( Object obj : root.elements()) {
					    Element urlElement = (Element) obj;
					    sb.setLength(0); 
					    if(urlElement.element("senai.bom.eap.version") != null) {
					    	// lê o documento novamente para alterar
					    	XMLWriter writer = new XMLWriter(new FileWriter(fileCommon));

					    	urlElement.element("senai.bom.eap.version").setText(pegaPomEAP(FileEAP));
					    	String alteradorEAP = urlElement.element("senai.bom.eap.version").getText();
					    	System.out.println(alteradorEAP);

					    	writer.write(doc);
					    	writer.close();


							// imprimi novamente o XML
							OutputFormat format = OutputFormat.createPrettyPrint();
							writer = new XMLWriter(System.out, format);
							writer.write(doc);
		
							// Compacta o XML
							format = OutputFormat.createCompactFormat();
							writer = new XMLWriter(System.out, format);
							writer.write(doc);
							break;

					    }
			    	}
			    	return common;
			    }
		return null;
	}
	
	public static String pegaPomEAP(String fileEap) throws DocumentException {
		
		SAXReader reader = new SAXReader();
		File filename = new File(fileEap);
		Document doc = reader.read(filename);
		 
		Element root = doc.getRootElement();
		    if(root.element("version").getText() != null) {
		    	String eap = root.element("version").getText(); 
		    	return eap;

			}
		return null;
	}
		
	
		
	}