package main;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.SomeValuesFromRestriction;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.RDFS;

public class OntologyManager {
	private final String TAG = this.getClass().getSimpleName();
	private ArrayList<String> lines;
	
	private String weedahmIRI = "http://www.semanticweb.org/weedahm/ontology";
	
	private String diseaseIRI = "#Dis"; //병명
	private String symptomIRI = "#Symp"; //증상(병증)
	private String toddlerIRI = "#Tod"; //소아
	private String dialecticIRI = "#Diale"; //변증
	private String medicineIRI = "#Med";
	private String medicineEffectIRI = "#MedEft";
	private String partIRI = "#Part"; //부위
	private String physiologyIRI = "#Phys"; //생리
	private String genderIRI = "#Gender"; //성별
	private String prescriptionIRI = "#Pre"; 
	private String reasonIRI = "#Reas"; //병인(병리)
	private String statusIRI = "#Status"; //상태
	private String womanIRI = "#Woman"; //여성
	private String hurtPartIRI = "#HurtPart"; //아픈부위
	private String etcIRI = "#ETC";
	
	private String smallName[] = {"disease","symptom","toddler", "dialectic", "physiology", "reason", "status", "woman", "hurtPart", "gender", "part"}; 
	
	private String hasSymptomURI = "#hasSymp";
	private String isSymptomOfURI = "#isSympOf"; //증상
	private String hasHurtPartURI = "#hasUrtPart";
	private String isHurtPartOfURI = "#isHurtPartOf"; //부위
	private String hasStatusOfWomanURI = "#hasStatusOfWoman";
	private String isStatusOfWomanURI = "#isStatusOfWoman"; //여성
	private String hasPhysURI = "#hasPhys"; 
	private String isPhysOfURI = "#isPhysOf"; //생리
	private String hasReasonURI = "#hasReason"; 
	private String isReasonOfURI = "#isReasonOf"; //병인
	private String hasToddlerURI = "#hasTod";
	private String isToddlerOfURI = "#isTodOf"; //소아
	private String hasDialecticURI = "#hasDiale";
	private String isDialecticOfURI = "#isDialeOf"; // 변증
	private String hasStatusURI = "#hasStatus";
	private String isStatusOfURI = "#isStatusOf"; //상태
	private String hasETCURI = "#hasETC";
	private String isETCOfURI = "#isETCOf"; 
	
	private String bigName[] = {"Symptom", "Toddler", "Dialectic", "Phys", "Reason", "Status", "StatusOfWoman", "HurtPart"};
	
	OntProperty hasSympProp, isSympOfProp, hasHurtPartProp, isHurtPartOfProp, hasStatusOfWomanProp, isStatusOfWomanOfProp, hasPhysProp, isPhysOfProp, hasReasonProp, isReasonOfProp, hasTodProp, isTodOfProp, hasDialeProp, isDialeOfProp, hasStatusProp, isStatusOfProp, hasETCProp, isETCOfProp;
	
	private String propName[] = {"Symp", "Tod", "Diale", "Phys", "Reason", "Status", "StatusOfWoman", "HurtPart"};
	
	
	
	private OntModel weedahmOnt = null;
	
	OntologyManager(ArrayList<String> lines) {
		this.lines = lines;
	}
	
	OntModel createModel() {
		weedahmOnt = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		initModel();
		Log.d(TAG, "Model Creation Complete");
		Log.d(TAG, "Start Composing Ontology");
		
		keywordCompositon();
		Log.d(TAG,  "KeyWord Composition Complete");
		
		CSVParser csvParser2 = new CSVParser("C:\\Users\\cow94\\Desktop\\keyword.csv");
		ArrayList<String> parseData2 = new ArrayList<>();
		try {
			parseData2 = csvParser2.parse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		readOntology(parseData2);
		makeOntology();
		Log.d(TAG,  "Ontology Composition Complete");
		
		OntClass test = weedahmOnt.getOntClass(weedahmIRI+symptomIRI);
		System.out.println(test);
		
		return weedahmOnt;
	}
	
	private void initModel() {
		
		weedahmOnt.createClass(weedahmIRI+diseaseIRI); //병명
		weedahmOnt.createClass(weedahmIRI+symptomIRI); //증상
		weedahmOnt.createClass(weedahmIRI+toddlerIRI); //소아
		weedahmOnt.createClass(weedahmIRI+dialecticIRI); //변증
		weedahmOnt.createClass(weedahmIRI+medicineIRI);
		weedahmOnt.createClass(weedahmIRI+medicineEffectIRI);
		weedahmOnt.createClass(weedahmIRI+partIRI); //부위
		weedahmOnt.createClass(weedahmIRI+physiologyIRI); //생리
		weedahmOnt.createClass(weedahmIRI+statusIRI); //상태
		weedahmOnt.createClass(weedahmIRI+reasonIRI); //병인
		weedahmOnt.createClass(weedahmIRI+genderIRI); //성별
		weedahmOnt.createClass(weedahmIRI+womanIRI); //여성
		weedahmOnt.createClass(weedahmIRI+etcIRI);
		
		/*
		//CreateClass
		for(int i=0; i<11; i++) {
			weedahmOnt.createClass(weedahmIRI+(smallName[i]+"IRI"));
		}
		*/

		//Set subclasses about woman and hurtpart
		OntClass hurtPartData = weedahmOnt.createClass(weedahmIRI + hurtPartIRI);
		weedahmOnt.getOntClass(weedahmIRI+partIRI).setSubClass(hurtPartData);
		OntClass womanData = weedahmOnt.createClass(weedahmIRI + womanIRI);
		weedahmOnt.getOntClass(weedahmIRI+genderIRI).setSubClass(womanData);
		
		//Set properties
		//증상
		hasSympProp = weedahmOnt.createOntProperty(weedahmIRI + hasSymptomURI);
		hasSympProp.setDomain(weedahmOnt.getResource(weedahmIRI + diseaseIRI));
		hasSympProp.setRange(weedahmOnt.getResource(weedahmIRI + symptomIRI));
		isSympOfProp = weedahmOnt.createOntProperty(weedahmIRI + isSymptomOfURI);
		isSympOfProp.setDomain(weedahmOnt.getResource(weedahmIRI + symptomIRI));
		isSympOfProp.setRange(weedahmOnt.getResource(weedahmIRI + diseaseIRI));
		//소아
		hasTodProp = weedahmOnt.createOntProperty(weedahmIRI + hasToddlerURI);
		hasTodProp.setDomain(weedahmOnt.getResource(weedahmIRI + diseaseIRI));
		hasTodProp.setRange(weedahmOnt.getResource(weedahmIRI + toddlerIRI));
		isTodOfProp = weedahmOnt.createOntProperty(weedahmIRI + isToddlerOfURI);
		isTodOfProp.setDomain(weedahmOnt.getResource(weedahmIRI + toddlerIRI));
		isTodOfProp.setRange(weedahmOnt.getResource(weedahmIRI + diseaseIRI));
		//변증
		hasDialeProp = weedahmOnt.createOntProperty(weedahmIRI + hasDialecticURI);
		hasDialeProp.setDomain(weedahmOnt.getResource(weedahmIRI + diseaseIRI));
		hasDialeProp.setRange(weedahmOnt.getResource(weedahmIRI + dialecticIRI));
		isDialeOfProp = weedahmOnt.createOntProperty(weedahmIRI + isDialecticOfURI);
		isDialeOfProp.setDomain(weedahmOnt.getResource(weedahmIRI + dialecticIRI));
		isDialeOfProp.setRange(weedahmOnt.getResource(weedahmIRI + diseaseIRI));
		//생리
		hasPhysProp = weedahmOnt.createOntProperty(weedahmIRI + hasPhysURI);
		hasPhysProp.setDomain(weedahmOnt.getResource(weedahmIRI + diseaseIRI));
		hasPhysProp.setRange(weedahmOnt.getResource(weedahmIRI + physiologyIRI));
		isPhysOfProp = weedahmOnt.createOntProperty(weedahmIRI + isPhysOfURI);
		isPhysOfProp.setDomain(weedahmOnt.getResource(weedahmIRI + physiologyIRI));
		isPhysOfProp.setRange(weedahmOnt.getResource(weedahmIRI + diseaseIRI));
		//상태
		hasStatusProp = weedahmOnt.createOntProperty(weedahmIRI + hasStatusURI);
		hasStatusProp.setDomain(weedahmOnt.getResource(weedahmIRI + diseaseIRI));
		hasStatusProp.setRange(weedahmOnt.getResource(weedahmIRI + statusIRI));
		isStatusOfProp = weedahmOnt.createOntProperty(weedahmIRI + isStatusOfURI);
		isStatusOfProp.setDomain(weedahmOnt.getResource(weedahmIRI + statusIRI));
		isStatusOfProp.setRange(weedahmOnt.getResource(weedahmIRI + diseaseIRI));
		//병인
		hasReasonProp = weedahmOnt.createOntProperty(weedahmIRI + hasReasonURI);
		hasReasonProp.setDomain(weedahmOnt.getResource(weedahmIRI + diseaseIRI));
		hasReasonProp.setRange(weedahmOnt.getResource(weedahmIRI + reasonIRI));
		isReasonOfProp = weedahmOnt.createOntProperty(weedahmIRI + isReasonOfURI);
		isReasonOfProp.setDomain(weedahmOnt.getResource(weedahmIRI + reasonIRI));
		isReasonOfProp.setRange(weedahmOnt.getResource(weedahmIRI + diseaseIRI));
		//부위
		hasHurtPartProp = weedahmOnt.createOntProperty(weedahmIRI + hasHurtPartURI);
		isHurtPartOfProp = weedahmOnt.createOntProperty(weedahmIRI + isHurtPartOfURI);
		hasHurtPartProp.setDomain(weedahmOnt.getResource(weedahmIRI + diseaseIRI));
		hasHurtPartProp.setRange(weedahmOnt.getResource(weedahmIRI + hurtPartIRI));
		isHurtPartOfProp.setDomain(weedahmOnt.getResource(weedahmIRI + hurtPartIRI));
		isHurtPartOfProp.setRange(weedahmOnt.getResource(weedahmIRI + diseaseIRI));
		//여성
		hasStatusOfWomanProp = weedahmOnt.createOntProperty(weedahmIRI + hasStatusOfWomanURI);
		hasStatusOfWomanProp.setDomain(weedahmOnt.getResource(weedahmIRI + womanIRI));
		isStatusOfWomanOfProp = weedahmOnt.createOntProperty(weedahmIRI + isStatusOfWomanURI);
		isStatusOfWomanOfProp.setRange(weedahmOnt.getResource(weedahmIRI + womanIRI));
		
		hasETCProp = weedahmOnt.createOntProperty(weedahmIRI + hasETCURI);
		isETCOfProp = weedahmOnt.createOntProperty(weedahmIRI + isETCOfURI);
		hasETCProp.setDomain(weedahmOnt.getResource(weedahmIRI + diseaseIRI));
		hasETCProp.setRange(weedahmOnt.getResource(weedahmIRI + etcIRI));
		isETCOfProp.setDomain(weedahmOnt.getResource(weedahmIRI + etcIRI));
		isETCOfProp.setRange(weedahmOnt.getResource(weedahmIRI + diseaseIRI));
	}
	
	
	private void keywordCompositon() {
		String csvSplitBy = ",";
		
		for(String aline: lines) {
			String data[] = aline.split(csvSplitBy);
			oneLineComposition(data);
		}
	}
	
	private void oneLineComposition(String[] data) {
		String compositionDataUri = "#"+data[0];
		OntClass compositionData = weedahmOnt.createClass(weedahmIRI + compositionDataUri);
		OntClass Woman = weedahmOnt.getOntClass(weedahmIRI + womanIRI);
		if(data.length < 2)
			weedahmOnt.getOntClass(weedahmIRI + etcIRI).addSubClass(compositionData);
				
		for(int i=1; i<data.length; i++) {
			switch(data[i]){
			case "노소" : weedahmOnt.getOntClass(weedahmIRI + toddlerIRI).addSubClass(compositionData);
						break;
			case "남여" : SomeValuesFromRestriction isStatusOfWoman = weedahmOnt.createSomeValuesFromRestriction(null, isStatusOfWomanOfProp, Woman);
						 compositionData.addSubClass(isStatusOfWoman);
						break;
			case "변증" : weedahmOnt.getOntClass(weedahmIRI + dialecticIRI).addSubClass(compositionData);
						break;
			case "생리" : weedahmOnt.getOntClass(weedahmIRI + physiologyIRI).addSubClass(compositionData);
						break;
			case "병리" : weedahmOnt.getOntClass(weedahmIRI + reasonIRI).addSubClass(compositionData);
						break;
			case "병인" : weedahmOnt.getOntClass(weedahmIRI + reasonIRI).addSubClass(compositionData);
						break;
			case "병증" : weedahmOnt.getOntClass(weedahmIRI + symptomIRI).addSubClass(compositionData);
						break;
			case "부위" : weedahmOnt.getOntClass(weedahmIRI + hurtPartIRI).addSubClass(compositionData);
						break;
			case "상태" : weedahmOnt.getOntClass(weedahmIRI + statusIRI).addSubClass(compositionData);
						break;
			case "증상" : weedahmOnt.getOntClass(weedahmIRI + symptomIRI).addSubClass(compositionData);
						break;
			}
		}
	}
	
	private void readOntology(ArrayList<String> lines) {
		this.lines = lines;
	}

	private void makeOntology() {
		String csvSplitBy = ",";
		
		for(String aline: lines) {
			String data2[] = aline.split(csvSplitBy);
			oneLineToOntology(data2);
		}
	}
	
	private void oneLineToOntology(String[] data) {
		if(data.length > 2){
		String disCode = data[0];
		String disNameURI = "#"+data[1];
		
		System.out.print(disCode);
		
		String symptomString = weedahmOnt.getOntClass(weedahmIRI+symptomIRI).toString();
		String hasHurtPartString = weedahmOnt.getOntClass(weedahmIRI+hurtPartIRI).toString();
		String reasonString = weedahmOnt.getOntClass(weedahmIRI+reasonIRI).toString();
		String dialecitcString = weedahmOnt.getOntClass(weedahmIRI+dialecticIRI).toString();
		
		OntClass disNameData = weedahmOnt.createClass(weedahmIRI + disNameURI);
		weedahmOnt.getOntClass(weedahmIRI + diseaseIRI).addSubClass(disNameData);
		if(disCode == null)
			System.out.print("nothing");
		
		
		for(int i=2; i<data.length; i++){
			String propDataURI = "#"+data[i];
			String propValue = null;
			OntClass propData = weedahmOnt.getOntClass(weedahmIRI + propDataURI);
			if(propData == null)
				System.out.print(", propNULL");
			else {
			ExtendedIterator<OntClass> propInfo =  propData.listSuperClasses();
			if(propInfo != null){
				propValue = propInfo.toString();
			}
			
			if(propValue == symptomString){
				System.out.print(", same");
				SomeValuesFromRestriction hasSymptom = weedahmOnt.createSomeValuesFromRestriction(null, hasSympProp, propData);
				disNameData.addSubClass(hasSymptom);
			}		
			if(propValue == hasHurtPartString){
				System.out.print(", same");
				SomeValuesFromRestriction hasHurtPart = weedahmOnt.createSomeValuesFromRestriction(null, hasHurtPartProp, propData);
				disNameData.addSubClass(hasHurtPart);
			}
			if(propValue == reasonString){
				System.out.print(", same");
				SomeValuesFromRestriction hasReason = weedahmOnt.createSomeValuesFromRestriction(null, hasReasonProp, propData);
				disNameData.addSubClass(hasReason);
			}
			if(propValue == dialecitcString){
				System.out.print(", same");
				SomeValuesFromRestriction hasDialectic = weedahmOnt.createSomeValuesFromRestriction(null, hasDialeProp, propData);
				disNameData.addSubClass(hasDialectic);
			}
			
		}
		
		}
		System.out.println();
		}
	}

	void printOnt() {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("C:\\Users\\cow94\\Desktop\\Ontology\\test15.owl");
			weedahmOnt.write(fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
	}
}