package tp.fil.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gmt.modisco.java.emf.impl.ClassDeclarationImpl;

@SuppressWarnings("restriction")
public class DataTransformation {

	private ResourceSet resSet;
	private Resource targetMM;
	private Resource sourceM;
	
	public DataTransformation(ResourceSet resSet, 
			                  Resource targetMM, 
			                  Resource sourceM) 
	{
		this.resSet = resSet;
		this.targetMM = targetMM;
		this.sourceM = sourceM;
	}

	public Resource runTransformation(String resourceURI) throws IOException {
		Resource targetM = resSet.createResource(URI.createFileURI(resourceURI));
		
		// Create model object that will contains classes
		EPackage ePackage = (EPackage) targetMM.getContents().get(0);
		EClass eModelClass = (EClass) ePackage.getEClassifier("Modele");
		EObject eModelObj = ePackage.getEFactoryInstance().create(eModelClass);
		
		// Transform all classDeclarations int classes
		Iterator<EObject> objIt = sourceM.getAllContents();
		List<EObject> eClassesList = new ArrayList<>();
		while(objIt.hasNext()) {
			EObject eObj = objIt.next();
			if(eObj.eClass().getName().equals("ClassDeclaration")) {
				EObject eClassObj = transformClassDeclaration2Classe((ClassDeclarationImpl) eObj);
				eClassesList.add(eClassObj);
			}
		}
		eModelObj.eSet(eModelClass.getEStructuralFeature("classes"), eClassesList);
		
		targetM.getContents().add(eModelObj);
		
		return targetM;
	}

	
	private EObject transformClassDeclaration2Classe(ClassDeclarationImpl s) {
		EPackage ePackage = (EPackage) targetMM.getContents().get(0);
		EClass eClasse = (EClass) ePackage.getEClassifier("Classe");
		EObject eObj = ePackage.getEFactoryInstance().create(eClasse);
		
		eObj.eSet(eClasse.getEStructuralFeature("nom"), s.getName());
		
		return eObj;
	}
	
}
