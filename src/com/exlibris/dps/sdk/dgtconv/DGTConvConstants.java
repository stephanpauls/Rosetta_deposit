package com.exlibris.dps.sdk.dgtconv;

public class DGTConvConstants {
	

 // production	
	public static final String userName = "u0077845";
	public static final String institution = "ROSETTA_KADOC";
	public static final String password = "Passw1rd";
	public static final String materialflowId = "59362";
	public static final String depositSetId = "1";

//test
/*	
	public static final String userName = "admin1";
	public static final String institution = "INS00";
	public static final String password = "a12345678A";
	public static final String materialflowId = "1466606";
	public static final String depositSetId = "1";	
*/	
	// should be placed under where submission format of MF is configured
	public static final String subDirectoryName = "/kadoc";
	
	public static final String metsRootFolder = "/nas/vol03/stephan/digital_entities/";
	public static final String folder_on_working_machine = "/nas/vol04/rosetta-deposit-prod/";
//	public static final String metsRootFolder = "M:/vol03/stephan/digital_entities/";
//	public static final String folder_on_working_machine = "M:/vol04/rosetta-deposit/";
	
//	public static final String metsRootFolderFileRelation = "C:\\digitool_mets\\MIGRATIE_BOEKEN";
	public static final String metsRootFolderFileRelation = "/nas/vol03/stephan/digital_entities/";

	public static final String filesRootFolder = folder_on_working_machine + subDirectoryName +"/content/streams";
	public static final String IEfullFileName = folder_on_working_machine + subDirectoryName + "/content/ie"; //1.xml";

/*	
	public static final String PDS_URL = "https://pds.libis.be/pds";
	public static final String DEPOSIT_WSDL_URL = "http://depot.lias.be/dpsws/deposit/DepositWebServices?wsdl";
	public static final String PRODUCER_WSDL_URL = "http://depot.lias.be/dpsws/backoffice/ProducerWebServices?wsdl";
	public static final String SIP_STATUS_WSDL_URL = "http://depot.lias.be/dpsws/repository/SipWebServices?wsdl";
*/	
	
	public static final String PDS_URL = "http://libis-p-rosetta-3.cc.kuleuven.be:8991/pds";
	public static final String DEPOSIT_WSDL_URL = "http://libis-p-rosetta-3.cc.kuleuven.be:1801/dpsws/deposit/DepositWebServices?wsdl";
	public static final String PRODUCER_WSDL_URL = "http://libis-p-rosetta-3.cc.kuleuven.be:1801/dpsws/backoffice/ProducerWebServices?wsdl";
	public static final String SIP_STATUS_WSDL_URL = "http://libis-p-rosetta-3.cc.kuleuven.be:1801/dpsws/repository/SipWebServices?wsdl";
	public static final String CONNECTION_WSDL_URL = "http://libis-p-rosetta-3.cc.kuleuven.be:1801/dpsws/repository/CollectionWebServices?wsdl";

	public static final int	DGT_WEBDOC = 1; 	
	public static final int DGT_EAD	 = 2;
	public static final int	DGT_DIG_POSTERS = 3;
	public static final int	DGT_DIG_PHOTO_ALBUMS = 4;
	public static final int	DGT_ARCHIVAL_IMAGES = 5;
	public static final int	DGT_BIBC_ExCathedra = 6;
	public static final int	DGT_BIBC_Manuscripts = 7;
	public static final int	DGT_BIBC_Legerbode = 8;
	public static final int	DGT_GBIB_OFMCapths = 9;
	public static final int	DGT_BIBC_ELPRENT = 10;
	public static final int	DGT_ETD_KUL = 11;
	public static final int	DGT_BOEKEN = 12;
	public static final int	DGT_KADOC_EPeriodieken = 13;
	public static final int	DGT_GBIB_BIF = 14;
	public static final int	DGT_KADOC_Trajecta = 15;
	public static final int	DGT_BIBC_LEUVEN = 16;
	public static final int	DGT_GBIB_Luther = 17;
	public static final int	DGT_GBIB_Anjou = 18;
	public static final int	DGT_GBIB_Augustinus = 19;
	public static final int	DGT_UBG_Flandrica = 20;
	public static final int	DGT_UA_Flandrica = 21;
	public static final int	DGT_EHC_Flandrica = 22;
	public static final int	DGT_PBL_Flandrica = 23;
	public static final int	DGT_OBB_Flandrica = 24;
	public static final int	DGT_BIBC_Flandrica = 25;
	public static final int	DGT_BIBC_POPP = 26;
	public static final int	DGT_CRKC_STANDAARD = 27;
	public static final int	DGT_CAG_STANDAARD = 28;
	public static final int	DGT_KADOC_SINT_LUCAS = 29;
	public static final int	DGT_KADOC_Capronnier = 30;
	public static final int	DGT_ETD_TM_TEST = 32;
	public static final int	DGT_BIBC_BOEK_17 = 33;
	public static final int	DGT_KUL_ILLUMINARE = 34;
	public static final int	DGT_BIBC_OUDE_THESIS = 35;
	public static final int	DGT_COLL_TEXTURA = 36;
	public static final int	DGT_ETD_KHBO = 37;
	public static final int	DGT_ETD_KATHO = 38;
	public static final int	DGT_ETD_TMM = 39;
	public static final int	DGT_ETD_TMK = 40;
	public static final int	DGT_ETD_KHLIM = 41;
	public static final int	DGT_ETD_KHL = 42;
	public static final int	DGT_DIG_FLAGS = 43;
	public static final int	DGT_STEPHAN_TILED = 44;
	public static final int	DGT_UB_VERWILGHEN = 45;
	public static final int	DGT_BIBC_BRIEFKAART = 46;
	public static final int	DGT_CBA_MILITAIRE_VERKENNING = 47;
	public static final int	DGT_KUL_EGYPTOLOGIE = 48;
	public static final int	DGT_RBINS_EUINSIDE = 49;
	public static final int	DGT_BIBC_MULTISPECTRAAL = 50;
	public static final int	DGT_2BERGEN_MOND = 51;
	public static final int	DGT_BIBC_BOEK_23 = 52;

		
	
	
	public static final String DGT_ENTITY_WEBDOC = "WebDoc"; 	
	public static final String DGT_ENTITY_EAD = "KADOC_EAD";
	public static final String DGT_ENTITY_DIG_POSTERS = "KADOC_Affiche";
	public static final String DGT_ENTITY_DIG_PHOTO_ALBUMS = "KADOC_PhotoAlbum";
	public static final String DGT_ENTITY_DIG_FLAGS = "KADOC_flags";
	public static final String DGT_ENTITY_ARCHIVAL_IMAGES = "KADOC_AchivalImages";
	public static final String DGT_ENTITY_BOEKEN = "KADOC_Boeken";
	public static final String DGT_ENTITY_BIBC_ExCathedra = "BIBC_ExCathedra";
	public static final String DGT_ENTITY_BIBC_Manuscripts = "BIBC_Manuscripts";
	public static final String DGT_ENTITY_BIBC_Legerbode = "BIBC_Legerbode";
	public static final String DGT_ENTITY_BIBC_BOEK = "BIBC_BoekKijker";
	public static final String DGT_ENTITY_GBIB_OFMCapths = "GBIB_OFMCapths";
	public static final String DGT_ENTITY_BIBC_ELPRENT = "BIBC_ELPRENT";
	public static final String DGT_ENTITY_KUL_ILLUMINARE = "KUL_Illuminare";
	public static final String DGT_ENTITY_BIBC_OUDE_THESIS = "BIBC_OudeThesis";
	public static final String DGT_ENTITY_ETD_KUL = "ETD_KUL";
	public static final String DGT_ENTITY_ETD_KHBO = "ETD_KHBO";
	public static final String DGT_ENTITY_ETD_KATHO = "ETD_KATHO";
	public static final String DGT_ENTITY_ETD_TMM = "ETD_TMM";
	public static final String DGT_ENTITY_ETD_TMK = "ETD_TMK";
	public static final String DGT_ENTITY_ETD_KHLIM = "ETD_KHLIM";
	public static final String DGT_ENTITY_ETD_KHL = "ETD_KHL";
	public static final String DGT_ENTITY_KADOC_Boeken = "KADOC_Boeken";
	public static final String DGT_ENTITY_KADOC_EPeriodieken = "KADOC_EPeriodieken";
	public static final String DGT_ENTITY_GBIB_BIF = "GBIB_BIF";
	public static final String DGT_ENTITY_KADOC_Trajecta = "KADOC_Trajecta";
	public static final String DGT_ENTITY_BIBC_LEUVEN = "BIBC_Leuven";
	public static final String DGT_ENTITY_GBIB_Luther = "GBIB_Luther";
	public static final String DGT_ENTITY_GBIB_Anjou = "GBIB_Anjou";
	public static final String DGT_ENTITY_GBIB_Augustinus = "GBIB_Augustinus";
	public static final String DGT_ENTITY_UBG_Flandrica = "UBG_Flandrica";
	public static final String DGT_ENTITY_UA_Flandrica = "UA_Flandrica";
	public static final String DGT_ENTITY_EHC_Flandrica = "EHC_Flandrica";
	public static final String DGT_ENTITY_PBL_Flandrica = "PBL_Flandrica";
	public static final String DGT_ENTITY_OBB_Flandrica = "OBB_Flandrica";
	public static final String DGT_ENTITY_BIBC_Flandrica = "BIBC_Flandrica";
	public static final String DGT_ENTITY_BIBC_POPP = "BIBC_Popp";
	public static final String DGT_ENTITY_CRKC_STANDAARD ="CRKC_Standaard";
	public static final String DGT_ENTITY_CAG_STANDAARD ="CAG_Standaard";
	public static final String DGT_ENTITY_KADOC_Sint_Lucas ="KADOC_Sint_Lucas";
	public static final String DGT_ENTITY_KADOC_Capronnier ="KADOC_Capronnier";
	public static final String DGT_ENTITY_ETD_TM_TEST = "ETD_TM_TEST";		
	public static final String DGT_ENTITY_COLL_TEXTURA = "KADOC_COLL_TEXTURA";
	public static final String DGT_ENTITY_STEPHAN_TILED = "Stephan_Tiled";
	public static final String DGT_ENTITY_UB_VERWILGHEN = "UB_Verwilghen";
	public static final String DGT_ENTITY_BIBC_BRIEFKAART= "BIBC_Briefkaarten";
	public static final String DGT_ENTITY_CBA_MILITAIRE_VERKENNING = "CBA_Militair";
	public static final String DGT_ENTITY_KUL_EGYPTOLOGIE = "KUL_Egyptologie";
	public static final String DGT_ENTITY_RBINS_EUINSIDE = "RBINS_EUInside";
	public static final String DGT_ENTITY_BIBC_MULTISPECTRAAL = "BIBC_Multispectraal";
	public static final String DGT_ENTITY_2BERGEN_MOND = "CBA_Mondgezondheid";
	
	
	public static final String RST_ACCESS_RIGHTS_EVERYONE = "AR_EVERYONE";
	public static final String RST_ACCESS_RIGHTS_IP_RANGE = "AR_IP_RANGE";
	public static final String RST_ACCESS_RIGHTS_IP_RANGE_REGISTERED = "AR_IP_RANGE_REGISTERED";
	public static final String RST_ACCESS_RIGHTS_CVBADMIN = "1504";
	public static final String RST_ACCESS_RIGHTS_KADOCADMIN = "1247";
	public static final String RST_ACCESS_RIGHTS_KADOCGUEST = "1248";
//	public static final String RST_ACCESS_RIGHTS_CVBADMIN = "1022";
//	public static final String RST_ACCESS_RIGHTS_KADOCADMIN = "1020";
//	public static final String RST_ACCESS_RIGHTS_KADOCGUEST = "1021";
	public static final String RST_ACCESS_RIGHTS_ETDADMIN = "17684";
	public static final String RST_ACCESS_RIGHTS_KADOC_CAPRONNIER = "89293";
	
}
