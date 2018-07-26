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
	private String StatusIRI = "#Status"; //상태
	private String WomanIRI = "#Woman"; //여성
	private String HurtPartIRI = "#HurtPart";
	
	
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
	
	
	OntProperty hasSympProp, isSympOfProp, hasHurtPartProp, isHurPartOfProp, hasStatusOfWomanProp, isStatusOfWomanOfProp, hasPhysProp, isPhysOfProp, hasReasonProp, isReasonOfProp, hasTodProp, isTodOfProp, hasDialeProp, isDialeOfProp, hasStatusProp, isStatusOfProp;
	
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
		
		CSVParser csvParser = new CSVParser("C:\\Users\\cow94\\Desktop\\demo2.csv");
		ArrayList<String> parseData = new ArrayList<>();
		try {
			parseData = csvParser.parse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		makeOntology();
		Log.d(TAG,  "Ontology Composition Complete");
		
		return weedahmOnt;
	}
	
	private void initModel() {
		weedahmOnt.createClass(weedahmIRI+diseaseIRI);
		weedahmOnt.createClass(weedahmIRI+symptomIRI);
		weedahmOnt.createClass(weedahmIRI+toddlerIRI);
		weedahmOnt.createClass(weedahmIRI+dialecticIRI);
		weedahmOnt.createClass(weedahmIRI+medicineIRI);
		weedahmOnt.createClass(weedahmIRI+medicineEffectIRI);
		weedahmOnt.createClass(weedahmIRI+partIRI);
		weedahmOnt.createClass(weedahmIRI+physiologyIRI);
		weedahmOnt.createClass(weedahmIRI+reasonIRI);
		weedahmOnt.createClass(weedahmIRI+genderIRI);
		weedahmOnt.createClass(prescriptionIRI+reasonIRI);
		
		
		
		hasSympProp = weedahmOnt.createOntProperty(weedahmIRI + hasSymptomURI);
		isSympOfProp = weedahmOnt.createOntProperty(weedahmIRI + isSymptomOfURI);
		hasSympProp.addInverseOf(isSympOfProp);
		hasSympProp.setDomain(weedahmOnt.getResource(weedahmIRI + diseaseIRI));
		hasSympProp.setRange(weedahmOnt.getResource(weedahmIRI + symptomIRI));
		
		hasHurtPartProp = weedahmOnt.createOntProperty(weedahmIRI + hasHurtPartURI);
		isHurPartOfProp = weedahmOnt.createOntProperty(weedahmIRI + isHurtPartOfURI);
		hasHurtPartProp.addInverseOf(isSympOfProp);
		hasHurtPartProp.setDomain(weedahmOnt.getResource(weedahmIRI + diseaseIRI));
		hasHurtPartProp.setRange(weedahmOnt.getResource(weedahmIRI + partIRI));
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
		
		for(int i=1; i<4; i++) {
			switch(data[i]){
			case "노소" : weedahmOnt.getOntClass(weedahmIRI + toddlerIRI).addSubClass(compositionData);
						break;
			case "남녀": weedahmOnt.getOntClass(weedahmIRI + genderIRI).addSubClass(compositionData);
						break;
			case "변증": weedahmOnt.getOntClass(weedahmIRI + physiologyIRI).addSubClass(compositionData);
						break;
			case "병리": weedahmOnt.getOntClass(weedahmIRI + reasonIRI).addSubClass(compositionData);
						break;
			case "병인": weedahmOnt.getOntClass(weedahmIRI + reasonIRI).addSubClass(compositionData);
						break;
			case "병증": weedahmOnt.getOntClass(weedahmIRI + symptomIRI).addSubClass(compositionData);
						break;
			case "부위": weedahmOnt.getOntClass(weedahmIRI + partIRI).addSubClass(compositionData);
						break;
			case "상태": weedahmOnt.getOntClass(weedahmIRI + symptomIRI).addSubClass(compositionData);
						break;
			case "증상": weedahmOnt.getOntClass(weedahmIRI + symptomIRI).addSubClass(compositionData);
						break;
			}
		}
	}
	
	private void makeOntology() {
		String csvSplitBy = ",";
		
		for(String aline: lines) {
			String data[] = aline.split(csvSplitBy);
			oneLineToOntology(data);
		}
	}
	
	// ���� ��з� �߰�
	private void oneLineToOntology(String[] data) {
		int symptomsLength = data.length - 2;
		
		// preparing URI
		String majorDemonstrationUri = "#"+data[0];
		String minorDemonstrationUri = "#"+data[1];
		ArrayList<String> symptomsUris = new ArrayList<>();
		for(int i = 2; i < 2+symptomsLength; i++) {
			symptomsUris.add("#"+data[i]);
		}
		
		// Create & Add Demonstrations
		OntClass majorDemonstration = weedahmOnt.createClass(weedahmIRI + majorDemonstrationUri);
		OntClass minorDemonstration = weedahmOnt.createClass(weedahmIRI + minorDemonstrationUri);
		weedahmOnt.getOntClass(weedahmIRI + diseaseIRI).addSubClass(majorDemonstration);
		majorDemonstration.addSubClass(minorDemonstration);
		
		// Create & Add Symptoms
		for(String symptomUri: symptomsUris) {		
			OntClass symptom = weedahmOnt.createClass(weedahmIRI + symptomUri);
			weedahmOnt.getOntClass(weedahmIRI + symptomIRI).addSubClass(symptom);
			SomeValuesFromRestriction hasSomeSymptom = weedahmOnt.createSomeValuesFromRestriction(null, hasSympProp, symptom);
			minorDemonstration.addSubClass(hasSomeSymptom);
		}
	}
	
	void printOnt() {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("C:\\Users\\cow94\\Desktop\\test.owl");
			weedahmOnt.write(fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
	}
}
