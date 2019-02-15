package tp.fil.main;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.gmt.modisco.java.emf.JavaPackage;

public class DataComputation {
	
	public static void main(String[] args) {
		try {
			Resource sourceM;
			Resource targetMM;
			
			ResourceSet resSet = new ResourceSetImpl();
			resSet.getResourceFactoryRegistry().
				getExtensionToFactoryMap().
				put("ecore", new EcoreResourceFactoryImpl());
			resSet.getResourceFactoryRegistry().
				getExtensionToFactoryMap().
				put("xmi", new XMIResourceFactoryImpl());
			
			JavaPackage.eINSTANCE.eClass();
			
			targetMM = resSet.createResource(URI.createFileURI("src/tp/fil/resources/Data.ecore"));
			targetMM.load(null);
			EPackage.Registry.INSTANCE.put("http://data", 
					targetMM.getContents().get(0));
			
			sourceM = resSet.createResource(URI.createFileURI("../PetStore/PetStore_java.xmi"));
			sourceM.load(null);
			
			DataTransformation dt = new DataTransformation(resSet, targetMM, sourceM);
			Resource targetM = dt.runTransformation("../PetStore/data_java.xmi");
			targetM.save(null);
			
			sourceM.unload();
			targetMM.unload();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
