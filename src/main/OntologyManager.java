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
	
	private String diseaseIRI = "#Dis";
	private String symptomIRI = "#Symp";
	private String ageIRI = "#Age";
	private String dialecticIRI = "#Diale";
	private String medicineIRI = "#Med";
	private String medicineEffectIRI = "#MedEft";
	private String partIRI = "#Part";
	private String physiologyIRI = "#Phys";
	private String genderIRI = "#Gender";
	private String prescriptionIRI = "#Pre";
	private String reasonIRI = "#Reas";
	
	
	private String hasSymptomURI = "#hasSymp";
	private String isSymptomOfURI = "#isSympOf";
	private String hasHurtPartURI = "#hasUrtPart";
	private String isHurtPartOfURI = "#isHurtPartOf";
	
	OntProperty hasSymptomProp, isSymptomOfProp, hasHurtPartProp, isHurPartOfProp;
	
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
		weedahmOnt.createClass(weedahmIRI+ageIRI);
		weedahmOnt.createClass(weedahmIRI+dialecticIRI);
		weedahmOnt.createClass(weedahmIRI+medicineIRI);
		weedahmOnt.createClass(weedahmIRI+medicineEffectIRI);
		weedahmOnt.createClass(weedahmIRI+partIRI);
		weedahmOnt.createClass(weedahmIRI+physiologyIRI);
		weedahmOnt.createClass(weedahmIRI+reasonIRI);
		weedahmOnt.createClass(weedahmIRI+genderIRI);
		weedahmOnt.createClass(prescriptionIRI+reasonIRI);
		
		
		
		hasSymptomProp = weedahmOnt.createOntProperty(weedahmIRI + hasSymptomURI);
		isSymptomOfProp = weedahmOnt.createOntProperty(weedahmIRI + isSymptomOfURI);
		hasSymptomProp.addInverseOf(isSymptomOfProp);
		hasSymptomProp.setDomain(weedahmOnt.getResource(weedahmIRI + diseaseIRI));
		hasSymptomProp.setRange(weedahmOnt.getResource(weedahmIRI + symptomIRI));
		
		hasHurtPartProp = weedahmOnt.createOntProperty(weedahmIRI + hasHurtPartURI);
		isHurPartOfProp = weedahmOnt.createOntProperty(weedahmIRI + isHurtPartOfURI);
		hasHurtPartProp.addInverseOf(isSymptomOfProp);
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
			case "노소" : weedahmOnt.getOntClass(weedahmIRI + ageIRI).addSubClass(compositionData);
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
			SomeValuesFromRestriction hasSomeSymptom = weedahmOnt.createSomeValuesFromRestriction(null, hasSymptomProp, symptom);
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
