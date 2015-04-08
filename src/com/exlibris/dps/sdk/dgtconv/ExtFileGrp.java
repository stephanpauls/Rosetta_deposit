package com.exlibris.dps.sdk.dgtconv;

import java.util.HashMap;

import com.exlibris.core.sdk.consts.Enum;
import com.exlibris.core.sdk.parser.IEParserException;
import com.exlibris.digitool.common.dnx.DNXConstants;
import com.exlibris.digitool.common.dnx.DnxDocument;
import com.exlibris.digitool.common.dnx.DnxDocumentHelper;
import com.exlibris.digitool.common.dnx.DnxSection;
import com.exlibris.digitool.common.dnx.DnxSectionRecord;
import com.exlibris.dps.sdk.deposit.IEParser;

import edu.harvard.hul.ois.mets.File;

import gov.loc.mets.MetsType.FileSec.FileGrp;

public class ExtFileGrp {
	
	private FileGrp fileGrpPreservation;
	private FileGrp fileGrpModified;
	private FileGrp fileGrpDerivative;
	private FileGrp fileGrpDerivativePdf;
	private FileGrp fileGrpDerivativeXMLMD;
	private FileGrp fileGrpDerivativeHtml;
	private FileGrp fileGrpDerivativeThmbnl;
	private FileGrp fileGrpDerivativeIndex;
	private boolean fileGrpPreservationFlag;
	private boolean fileGrpModifiedFlag;
	private boolean fileGrpDerivativeFlag;
	private boolean fileGrpDerivativePdfFlag;
	private boolean fileGrpDerivativeXMLMDFlag;
	private boolean fileGrpDerivativeHtmlFlag;
	private boolean fileGrpDerivativeThmbnlFlag;
	private boolean fileGrpDerivativeIndexFlag;
	private HashMap<Integer, FileInfo> ordFilePreservationList;
	private HashMap<Integer, FileInfo> ordFileModifiedList;
	private HashMap<Integer, FileInfo> ordFileDerivativeList;
	private HashMap<Integer, FileInfo> ordFileDerivativeXMLMDList;
	private HashMap<Integer, FileInfo> ordFileDerivativeHtmlList;
	private HashMap<Integer, FileInfo> ordFileDerivativeThmbnlList;
	private HashMap<Integer, FileInfo> ordFileDerivativeIndexList;
	private HashMap<Integer, FileInfo> ordFileDerivativePdfList;
	private FileInfo fileInfo;
	private Integer fileNr=0;
	
	
	private IEParser ie;
	private String repLabel;
	

	public void setFileInfo(String filenaam, String pid, File file, Integer nr,String dmdid,String use) {
		fileInfo = new FileInfo();
        fileInfo.setNaam(filenaam);
        fileInfo.setPID(pid);
        fileInfo.setFile(file);
        fileInfo.setDMDID(dmdid);
        fileInfo.setUSE(use);
        fileNr = nr;
	}
	
	public ExtFileGrp(IEParser ieParser,String label) {
		ie = ieParser;
		repLabel = label;
		resetFlags();
	}

	public FileGrp generateModifiedFgrp() {
		try{
		if (fileGrpModified == null) {
			fileGrpModified = (FileGrp) ie.addNewFileGrp(Enum.UsageType.VIEW, Enum.PreservationType.DERIVATIVE_COPY);
			DnxDocument dnxDocument = ie.getFileGrpDnx(fileGrpModified.getID());
			DnxDocumentHelper documentHelper = new DnxDocumentHelper(dnxDocument);
			documentHelper.getGeneralRepCharacteristics().setLabel("VIEW "+repLabel);
			documentHelper.getGeneralRepCharacteristics().setRepresentationCode("HIGH");
			ie.setFileGrpDnx(documentHelper.getDocument(), fileGrpModified.getID());
			fileGrpModifiedFlag = true;
			ordFileModifiedList = new HashMap();
		}
		} catch (IEParserException e) {
			System.out.println(e.getMessage());
		}
		return getFileGrpModified();
	}

	
	public FileGrp generatePreservationFgrp() {
		try {
			if (fileGrpPreservation == null) {
				fileGrpPreservation = (FileGrp) ie.addNewFileGrp(Enum.UsageType.VIEW, Enum.PreservationType.PRESERVATION_MASTER);
				fileGrpPreservationFlag = true;
				ordFilePreservationList = new HashMap();
			}
		} catch (IEParserException e) {
			System.out.println(e.getMessage());
		}
		return getFileGrpPreservation();
	}
	
	public FileGrp generateDerivativePdfFgrp() {
		try{
		if (fileGrpDerivativePdf == null) {
			fileGrpDerivativePdf = (FileGrp) ie.addNewFileGrp(Enum.UsageType.VIEW, Enum.PreservationType.DERIVATIVE_COPY);
			DnxDocument dnxDocument = ie.getFileGrpDnx(fileGrpDerivativePdf.getID());
			DnxDocumentHelper documentHelper = new DnxDocumentHelper(dnxDocument);
			documentHelper.getGeneralRepCharacteristics().setLabel("VIEW (PDF) "+repLabel);
			documentHelper.getGeneralRepCharacteristics().setRepresentationCode("LOW");
			ie.setFileGrpDnx(documentHelper.getDocument(), fileGrpDerivativePdf.getID());
			fileGrpDerivativePdfFlag = true;
			ordFileDerivativePdfList = new HashMap();
			
		}
		} catch (IEParserException e) {
			System.out.println(e.getMessage());
		}
		return getFileGrpDerivativePdf();
	}

	public FileGrp generateDerivativeXMLMDFgrp() {
		try{
		if (fileGrpDerivativeXMLMD == null) {
			fileGrpDerivativeXMLMD = (FileGrp) ie.addNewFileGrp(Enum.UsageType.VIEW, Enum.PreservationType.DERIVATIVE_COPY);
			
			DnxDocument dnxDocument = ie.getFileGrpDnx(fileGrpDerivativeXMLMD.getID());
			DnxDocumentHelper documentHelper = new DnxDocumentHelper(dnxDocument);
			documentHelper.getGeneralRepCharacteristics().setLabel("VIEW (XML MD) "+repLabel);
			documentHelper.getGeneralRepCharacteristics().setRepresentationCode("LOW");
			ie.setFileGrpDnx(documentHelper.getDocument(), fileGrpDerivativeXMLMD.getID());
			fileGrpDerivativeXMLMDFlag = true;
			ordFileDerivativeXMLMDList = new HashMap();
			
		}
		} catch (IEParserException e) {
			System.out.println(e.getMessage());
		}
		return getFileGrpDerivativeXMLMD();
	}
	
	public FileGrp generateDerivativeHtmlFgrp() {
		try{
		if (fileGrpDerivativeHtml == null) {
			fileGrpDerivativeHtml = (FileGrp) ie.addNewFileGrp(Enum.UsageType.VIEW, Enum.PreservationType.DERIVATIVE_COPY);
			
			DnxDocument dnxDocument = ie.getFileGrpDnx(fileGrpDerivativeHtml.getID());
			DnxDocumentHelper documentHelper = new DnxDocumentHelper(dnxDocument);
			documentHelper.getGeneralRepCharacteristics().setLabel("VIEW (html) "+repLabel);
			documentHelper.getGeneralRepCharacteristics().setRepresentationCode("LOW");
			ie.setFileGrpDnx(documentHelper.getDocument(), fileGrpDerivativeHtml.getID());
			fileGrpDerivativeHtmlFlag = true;
			ordFileDerivativeHtmlList = new HashMap();
			
		}
		} catch (IEParserException e) {
			System.out.println(e.getMessage());
		}
		return getFileGrpDerivativeHtml();
	}
	
	public FileGrp generateDerivativeFgrp() {
		try{
		if (fileGrpDerivative == null) {
			fileGrpDerivative = (FileGrp) ie.addNewFileGrp(Enum.UsageType.VIEW, Enum.PreservationType.DERIVATIVE_COPY);
			
			DnxDocument dnxDocument = ie.getFileGrpDnx(fileGrpDerivative.getID());
			DnxDocumentHelper documentHelper = new DnxDocumentHelper(dnxDocument);
			documentHelper.getGeneralRepCharacteristics().setLabel("VIEW_MAIN "+repLabel);
			documentHelper.getGeneralRepCharacteristics().setRepresentationCode("LOW");
			ie.setFileGrpDnx(documentHelper.getDocument(), fileGrpDerivative.getID());
			fileGrpDerivativeFlag = true;
			ordFileDerivativeList = new HashMap();
			
		}
		} catch (IEParserException e) {
			System.out.println(e.getMessage());
		}
		return getFileGrpDerivative();
	}
	
	public FileGrp generateDerivativeThmbnlFgrp() {
		try{
		if (fileGrpDerivativeThmbnl == null) {
			fileGrpDerivativeThmbnl = (FileGrp) ie.addNewFileGrp(Enum.UsageType.VIEW, Enum.PreservationType.DERIVATIVE_COPY);
			
			DnxDocument dnxDocument = ie.getFileGrpDnx(fileGrpDerivativeThmbnl.getID());
			DnxDocumentHelper documentHelper = new DnxDocumentHelper(dnxDocument);
			documentHelper.getGeneralRepCharacteristics().setLabel("THUMBNAIL "+repLabel);
			documentHelper.getGeneralRepCharacteristics().setRepresentationCode("THUMBNAIL");
			ie.setFileGrpDnx(documentHelper.getDocument(), fileGrpDerivativeThmbnl.getID());
			fileGrpDerivativeThmbnlFlag = true;
			ordFileDerivativeThmbnlList = new HashMap();
			
		}
		} catch (IEParserException e) {
			System.out.println(e.getMessage());
		}
		return getFileGrpDerivativeThmbnl();
	}
	
	public FileGrp generateDerivativeIndexFgrp() {
		try{
		if (fileGrpDerivativeIndex == null) {
			fileGrpDerivativeIndex = (FileGrp) ie.addNewFileGrp(Enum.UsageType.VIEW, Enum.PreservationType.DERIVATIVE_COPY);
			
			DnxDocument dnxDocument = ie.getFileGrpDnx(fileGrpDerivativeIndex.getID());
			DnxDocumentHelper documentHelper = new DnxDocumentHelper(dnxDocument);
			documentHelper.getGeneralRepCharacteristics().setLabel("INDEX "+repLabel);
			documentHelper.getGeneralRepCharacteristics().setRepresentationCode("INDEX");
			ie.setFileGrpDnx(documentHelper.getDocument(), fileGrpDerivativeIndex.getID());
			fileGrpDerivativeIndexFlag = true;
			ordFileDerivativeIndexList = new HashMap();

		}
		} catch (IEParserException e) {
			System.out.println(e.getMessage());
		}
		return getFileGrpDerivativeIndex();
	}
	
	
	public void clean () {
		fileGrpDerivative = null;
		fileGrpModified = null;
		fileGrpPreservation = null;
		fileGrpDerivativePdf = null;
		fileGrpDerivativeXMLMD = null;
		fileGrpDerivativeThmbnl = null;
		fileGrpDerivativeIndex = null;
		resetFlags();
	}
	
	public void clearInfo() {
		fileNr = -1;
	}
	public FileGrp getFileGrpPreservation() {
		if (fileGrpPreservation != null && fileNr >=0) ordFilePreservationList.put(this.fileNr, this.fileInfo);
		return fileGrpPreservation;
		
	}
	public void setFileGrpPreservation(FileGrp fileGrpPreservation) {
		this.fileGrpPreservation = fileGrpPreservation;
	}
	public FileGrp getFileGrpModified() {
		if (fileGrpModified != null && fileNr >=0) ordFileModifiedList.put(this.fileNr, this.fileInfo);
		return fileGrpModified;
	}
	public void setFileGrpModified(FileGrp fileGrpModified) {
		this.fileGrpModified = fileGrpModified;
	}
	public FileGrp getFileGrpDerivative() {
		if (fileGrpDerivative != null && fileNr >=0) ordFileDerivativeList.put(this.fileNr, this.fileInfo);
		return fileGrpDerivative;
	}
	public void setFileGrpDerivative(FileGrp fileGrpDerivative) {
		this.fileGrpDerivative = fileGrpDerivative;
	}
	public FileGrp getFileGrpDerivativePdf() {
		if (fileGrpDerivativePdf != null && fileNr >=0) ordFileDerivativePdfList.put(this.fileNr, this.fileInfo);
		return fileGrpDerivativePdf;
	}
	public void setFileGrpDerivativePdf(FileGrp fileGrpDerivativePdf) {
		this.fileGrpDerivativePdf = fileGrpDerivativePdf;
	}

	public FileGrp getFileGrpDerivativeThmbnl() {
		if (fileGrpDerivativeThmbnl != null && fileNr >=0) ordFileDerivativeThmbnlList.put(this.fileNr, this.fileInfo);
		return fileGrpDerivativeThmbnl;
	}

	public void setFileGrpDerivativeThmbnl(FileGrp fileGrpDerivativeThmbnl) {
		this.fileGrpDerivativeThmbnl = fileGrpDerivativeThmbnl;
	}

	public FileGrp getFileGrpDerivativeXMLMD() {
		if (fileGrpDerivativeXMLMD != null && fileNr >=0) ordFileDerivativeXMLMDList.put(this.fileNr, this.fileInfo);
		return fileGrpDerivativeXMLMD;
	}

	public void setFileGrpDerivativeXMLMD(FileGrp fileGrpDerivativeXMLMD) {
		this.fileGrpDerivativeXMLMD = fileGrpDerivativeXMLMD;
	}

	public FileGrp getFileGrpDerivativeHtml() {
		if (fileGrpDerivativeHtml != null && fileNr >=0) ordFileDerivativeHtmlList.put(this.fileNr, this.fileInfo);
		return fileGrpDerivativeHtml;
	}

	public void setFileGrpDerivativeHtml(FileGrp fileGrpDerivativeHtml) {
		this.fileGrpDerivativeHtml = fileGrpDerivativeHtml;
	}

	public FileGrp getFileGrpDerivativeIndex() {
		if (fileGrpDerivativeIndex != null && fileNr >=0) ordFileDerivativeIndexList.put(this.fileNr, this.fileInfo);
		return fileGrpDerivativeIndex;
	}
	
	public boolean isFileGrpPreservationFlag() {
		return fileGrpPreservationFlag;
	}

	public boolean isFileGrpModifiedFlag() {
		return fileGrpModifiedFlag;
	}

	public boolean isFileGrpDerivativeFlag() {
		return fileGrpDerivativeFlag;
	}

	public boolean isFileGrpDerivativePdfFlag() {
		return fileGrpDerivativePdfFlag;
	}

	public boolean isFileGrpDerivativeXMLMDFlag() {
		return fileGrpDerivativeXMLMDFlag;
	}

	public boolean isFileGrpDerivativeHtmlFlag() {
		return fileGrpDerivativeHtmlFlag;
	}

	public boolean isFileGrpDerivativeThmbnlFlag() {
		return fileGrpDerivativeThmbnlFlag;
	}
	

	public boolean isFileGrpDerivativeIndexFlag() {
		return fileGrpDerivativeIndexFlag;
	}

	public HashMap<Integer, FileInfo> getOrdFilePreservationList() {
		return ordFilePreservationList;
	}

	public HashMap<Integer, FileInfo> getOrdFileModifiedList() {
		return ordFileModifiedList;
	}

	public HashMap<Integer, FileInfo> getOrdFileDerivativeList() {
		return ordFileDerivativeList;
	}

	public HashMap<Integer, FileInfo> getOrdFileDerivativeXMLMDList() {
		return ordFileDerivativeXMLMDList;
	}

	public HashMap<Integer, FileInfo> getOrdFileDerivativeHtmlList() {
		return ordFileDerivativeHtmlList;
	}

	public HashMap<Integer, FileInfo> getOrdFileDerivativeThmbnlList() {
		return ordFileDerivativeThmbnlList;
	}

	public HashMap<Integer, FileInfo> getOrdFileDerivativeIndexList() {
		return ordFileDerivativeIndexList;
	}

	public HashMap<Integer, FileInfo> getOrdFileDerivativePdfList() {
		return ordFileDerivativePdfList;
	}
	
	public void resetFlags() {
		fileGrpPreservationFlag = false;
		fileGrpModifiedFlag = false;
		fileGrpDerivativeFlag = false;
		fileGrpDerivativePdfFlag = false;
		fileGrpDerivativeXMLMDFlag = false;
		fileGrpDerivativeHtmlFlag = false;
		fileGrpDerivativeThmbnlFlag = false;		
		fileGrpDerivativeIndexFlag = false;		
		ordFilePreservationList = null;
		ordFileModifiedList = null;
		ordFileDerivativeList = null;
		ordFileDerivativeXMLMDList = null;
		ordFileDerivativeHtmlList = null;
		ordFileDerivativeThmbnlList = null;
		ordFileDerivativeIndexList = null;
		ordFileDerivativePdfList = null;	}
	
}
