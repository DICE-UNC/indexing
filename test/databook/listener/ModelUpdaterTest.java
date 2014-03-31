package databook.listener;

import static databook.edsl.map.Utils.json;
import static databook.utils.ModelUtils.ATTRIBUTE_URI;
import static databook.utils.ModelUtils.AVU_URI;
import static databook.utils.ModelUtils.COLLECTION_URI;
import static databook.utils.ModelUtils.DATABOOK_MODEL_URI;
import static databook.utils.ModelUtils.DATA_OBJECT_URI;
import static databook.utils.ModelUtils.DATA_SIZE_URI;
import static databook.utils.ModelUtils.HAS_PART_URI;
import static databook.utils.ModelUtils.IS_A;
import static databook.utils.ModelUtils.LABEL_MODEL_URI;
import static databook.utils.ModelUtils.LABEL_URI;
import static databook.utils.ModelUtils.METADATA_URI;
import static databook.utils.ModelUtils.UNIT_URI;
import static databook.utils.ModelUtils.VALUE_URI;
import static databook.utils.ModelUtils.bracket;
import static databook.utils.ModelUtils.databookDecimal;
import static databook.utils.ModelUtils.databookResource;
import static databook.utils.ModelUtils.databookResourceNoBracket;
import static databook.utils.ModelUtils.databookStatement;
import static databook.utils.ModelUtils.databookString;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.endsWith;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.fasterxml.jackson.databind.ObjectMapper;

import databook.edsl.map.Utils;
import databook.local.model.RDFDatabase;
import databook.local.model.RDFDatabase.RDFDatabaseTransaction;
import databook.local.model.RDFServiceWrapper;

public class ModelUpdaterTest {

	ModelUpdater modelUpdater;
	RDFDatabase rdfDatabase;
	RDFDatabaseTransaction rdfDatabaseTransaction;
	databook.local.model.RDFServiceWrapper rdfService;
	private Indexer vivoIndex;
	
	@Before
	public void setupModelUpdater() throws Exception {
		rdfDatabase = mock(RDFDatabase.class, withSettings().verboseLogging());
		rdfDatabaseTransaction = mock(RDFDatabaseTransaction.class, withSettings().verboseLogging());
		when(rdfDatabase.newTransaction()).thenReturn(rdfDatabaseTransaction);
		modelUpdater = new ModelUpdater();
//		vivoIndex = new VIVOIndexer(rdfDatabase);
//		modelUpdater.regIndexer(vivoIndex);
		rdfService = mock(RDFServiceWrapper.class, withSettings().verboseLogging());
		when(rdfDatabase.selectQuery()).thenReturn(databook.edsl.googql.Utils.query().use(rdfService));
		when(rdfService.sparqlSelectQuery(anyString())).thenReturn(new ByteArrayInputStream("".getBytes()));
		
	}
	
	@After
	public void tearDownModelUpdater() {
		modelUpdater = null;
		rdfDatabase = null;
		rdfDatabaseTransaction = null;
	}
	
	@Test
	public void testAddDataObject() throws Exception {
		HashMap<String, Object> map = (HashMap<String, Object>) 
			Utils.json().messages()
				.create().dataObject()
					.uri(databookResourceNoBracket("obj1"))
					.at().tripleStore().end()
					.label("/zone/dir/obj")
				.end()
				.union()
					.collection().uri(databookResourceNoBracket("col1")).end()
					.collection().hasPart().dataObject().uri(databookResourceNoBracket("obj1")).end().end().end()
			.end().run();
		String msg = new ObjectMapper().writeValueAsString(map);
		
		modelUpdater.update(msg);
		
		InOrder inOrder = inOrder(rdfDatabaseTransaction);
		inOrder.verify(rdfDatabaseTransaction).start();
		inOrder.verify(rdfDatabaseTransaction).add(
				databookStatement(databookResource("obj1"), IS_A, bracket(DATA_OBJECT_URI)), 
				RDFDatabase.Format.N3, 
				DATABOOK_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).add(
				databookStatement(databookResource("obj1"), bracket(LABEL_URI), databookString("/zone/dir/obj")), 
				RDFDatabase.Format.N3, 
				LABEL_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).add(
				databookStatement(databookResource("col1"), bracket(HAS_PART_URI), databookResource("obj1")), 
				RDFDatabase.Format.N3, 
				DATABOOK_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).commit();

	}

	@Test
	public void testAddCollection() throws Exception {
		HashMap<String, Object> map = (HashMap<String, Object>) 
				Utils.json().messages()
					.create().collection()
						.uri(databookResourceNoBracket("sub1"))
						.at().tripleStore().end()
						.label("/zone/dir/sub")
					.end()
					.union()
						.collection().uri(databookResourceNoBracket("col1")).end()
						.collection().hasPart().dataObject().uri(databookResourceNoBracket("sub1")).end().end().end()
				.end().run();
			String msg = new ObjectMapper().writeValueAsString(map);
		
		modelUpdater.update(msg);
		
		InOrder inOrder = inOrder(rdfDatabaseTransaction);
		inOrder.verify(rdfDatabaseTransaction).start();
		inOrder.verify(rdfDatabaseTransaction).add(
				databookStatement(databookResource("sub1"), IS_A, bracket(COLLECTION_URI)), 
				RDFDatabase.Format.N3, 
				DATABOOK_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).add(
				databookStatement(databookResource("sub1"), bracket(LABEL_URI), databookString("/zone/dir/sub")), 
				RDFDatabase.Format.N3, 
				LABEL_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).add(
				databookStatement(databookResource("col1"), bracket(HAS_PART_URI), databookResource("sub1")), 
				RDFDatabase.Format.N3, 
				DATABOOK_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).commit();

	}
//	when(rdfDatabase.getEntityTypeUri(databookResourceNoBracket("obj1"))).thenReturn(DATA_OBJECT_URI);
//	when(rdfDatabase.getEntityTypeUri(databookResourceNoBracket("col1"))).thenReturn(COLLECTION_URI);
//	when(rdfDatabase.getEntityTypeUri(databookResourceNoBracket("sub1"))).thenReturn(COLLECTION_URI);
	
	@Test
	public void testDelCollection() throws Exception  {
		when(rdfService.sparqlSelectQuery(contains(LABEL_URI))).thenReturn(new ByteArrayInputStream("/zone/dir/sub\n".getBytes()));
		
		HashMap map = (HashMap) json().messages()
				.diff()
					.collection().uri(databookResourceNoBracket("col1")).end()
					.collection().uri(databookResourceNoBracket("col1"))
						.hasPart()
							.collection().uri(databookResourceNoBracket("sub1")).end()
						.end()
					.end()
				.delete()
					.collection()
						.uri(databookResourceNoBracket("sub1")).at().tripleStore().end()
					.end()
			.end().run();
		
		String msg = new ObjectMapper().writeValueAsString(map);

		
		modelUpdater.update(msg);
		
		InOrder inOrder = inOrder(rdfDatabaseTransaction);
		inOrder.verify(rdfDatabaseTransaction).start();
		inOrder.verify(rdfDatabaseTransaction).remove(
				databookStatement(databookResource("col1"), bracket(HAS_PART_URI), databookResource("sub1")), 
				RDFDatabase.Format.N3, 
				DATABOOK_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).remove(
				databookStatement(databookResource("sub1"), bracket(LABEL_URI), databookString("/zone/dir/sub")), 
				RDFDatabase.Format.N3, 
				LABEL_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).remove(
				databookStatement(databookResource("sub1"), IS_A, bracket(COLLECTION_URI)), 
				RDFDatabase.Format.N3, 
				DATABOOK_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).commit();
		
	}
	
	@Test
	public void testDelDataObj() throws Exception  {
		when(rdfService.sparqlSelectQuery(contains(LABEL_URI))).thenReturn(new ByteArrayInputStream("/zone/dir/obj\n".getBytes()));
		HashMap map = (HashMap) json().messages()
				.diff()
					.collection().uri(databookResourceNoBracket("col1")).end()
					.collection().uri(databookResourceNoBracket("col1"))
						.hasPart()
							.dataObject().uri(databookResourceNoBracket("obj1")).end()
						.end()
					.end()
				.delete()
					.dataObject()
						.uri(databookResourceNoBracket("obj1")).at().tripleStore().end()
					.end()
			.end().run();
		
		String msg = new ObjectMapper().writeValueAsString(map);

		
		modelUpdater.update(msg);
		
		InOrder inOrder = inOrder(rdfDatabaseTransaction);
		inOrder.verify(rdfDatabaseTransaction).start();
		inOrder.verify(rdfDatabaseTransaction).remove(
				databookStatement(databookResource("col1"), bracket(HAS_PART_URI), databookResource("obj1")), 
				RDFDatabase.Format.N3, 
				DATABOOK_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).remove(
				databookStatement(databookResource("obj1"), bracket(LABEL_URI), databookString("/zone/dir/obj")), 
				RDFDatabase.Format.N3, 
				LABEL_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).remove(
				databookStatement(databookResource("obj1"), IS_A, bracket(DATA_OBJECT_URI)), 
				RDFDatabase.Format.N3, 
				DATABOOK_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).commit();
		
	}
	
	@Test
	public void testRenameCollection() throws Exception  {
//		when(rdfDatabase.getEntityTypeUri(databookResourceNoBracket("sub1"))).thenReturn(COLLECTION_URI);
//		when(rdfDatabase.exists(databookResourceNoBracket("sub1"))).thenReturn(true);
		//when(rdfDatabase.getValues(databookResourceNoBracket("sub1"), LABEL_URI)).thenReturn(Arrays.asList(new String[]{"/zone/dir/sub"}));
//		when(rdfDatabase.getUniqueValue(databookResourceNoBracket("sub1"), LABEL_URI)).thenReturn("/zone/dir/sub");
		when(rdfService.sparqlSelectQuery(contains(LABEL_URI))).thenReturn(new ByteArrayInputStream("/zone/dir/sub\n".getBytes()));
		HashMap map = (HashMap) json().messages()
				.modify()
					.collection()
						.uri(databookResourceNoBracket("sub1"))
						//.label("/zone/dir/sub")
					.end()
					.collection()
						.label("/zone/dir/foo")
					.end()
			.end().run();
		
		String msg = new ObjectMapper().writeValueAsString(map);

		
		modelUpdater.update(msg);
		
		InOrder inOrder = inOrder(rdfDatabaseTransaction);
		inOrder.verify(rdfDatabaseTransaction).start();
		/*inOrder.verify(rdfDatabaseTransaction).remove(
				databookStatement(databookResource("col1"), bracket(HAS_PART_URI), databookResource("sub1")), 
				RDFDatabase.Format.N3, 
				DATABOOK_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).add(
				databookStatement(databookResource("col1"), bracket(HAS_PART_URI), databookResource("sub1")), 
				RDFDatabase.Format.N3, 
				DATABOOK_MODEL_URI
		);*/
		inOrder.verify(rdfDatabaseTransaction).remove(
				databookStatement(databookResource("sub1"), bracket(LABEL_URI), databookString("/zone/dir/sub")), 
				RDFDatabase.Format.N3, 
				LABEL_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).add(
				databookStatement(databookResource("sub1"), bracket(LABEL_URI), databookString("/zone/dir/foo")), 
				RDFDatabase.Format.N3, 
				LABEL_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).commit();
		
	}
	
	@Test
	public void testMoveCollection() throws Exception  {
		when(rdfDatabase.getEntityTypeUri(databookResourceNoBracket("sub1"))).thenReturn(COLLECTION_URI);
		when(rdfDatabase.exists(databookResourceNoBracket("sub1"))).thenReturn(true);
		// when(rdfDatabase.getValues(databookResourceNoBracket("sub1"), LABEL_URI)).thenReturn(Arrays.asList(new String[]{"/zone/dir/sub"}));
		when(rdfDatabase.getUniqueValue(databookResourceNoBracket("sub1"), LABEL_URI)).thenReturn("/zone/dir/sub");
		when(rdfService.sparqlSelectQuery(contains(LABEL_URI))).thenAnswer(new Answer<InputStream>(){
			
			@Override
			public InputStream answer(InvocationOnMock invocation)
					throws Throwable {
				return (new ByteArrayInputStream("/zone/dir/sub\n".getBytes()));
			}
			
		});
		
		HashMap map = (HashMap) json().messages()
				.diff()
					.collection()
						.uri(databookResourceNoBracket("col1"))
					.end()
					.collection()
						.hasPart()
							.collection().uri(databookResourceNoBracket("sub1")).end()
						.end()
					.end()
				.union()
					.collection()
						.uri(databookResourceNoBracket("hom1"))
					.end()
					.collection()
						.hasPart()
							.collection().uri(databookResourceNoBracket("sub1")).end()
						.end()
					.end()
				.modify()
					.collection()
						.uri(databookResourceNoBracket("sub1"))
					.end()
					.collection()
						.label("/zone/hom/sub")
					.end()
			.end().run();
		
		String msg = new ObjectMapper().writeValueAsString(map);
		
		modelUpdater.update(msg);
		
		InOrder inOrder = inOrder(rdfDatabaseTransaction);
		inOrder.verify(rdfDatabaseTransaction).start();
		inOrder.verify(rdfDatabaseTransaction).remove(
				databookStatement(databookResource("col1"), bracket(HAS_PART_URI), databookResource("sub1")), 
				RDFDatabase.Format.N3, 
				DATABOOK_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).add(
				databookStatement(databookResource("hom1"), bracket(HAS_PART_URI), databookResource("sub1")), 
				RDFDatabase.Format.N3, 
				DATABOOK_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).remove(
				databookStatement(databookResource("sub1"), bracket(LABEL_URI), databookString("/zone/dir/sub")), 
				RDFDatabase.Format.N3, 
				LABEL_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).add(
				databookStatement(databookResource("sub1"), bracket(LABEL_URI), databookString("/zone/hom/sub")), 
				RDFDatabase.Format.N3, 
				LABEL_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).commit();
		
	}
	
	@Test
	public void testRenameDataObj() throws Exception  {
		when(rdfDatabase.getEntityTypeUri(databookResourceNoBracket("obj1"))).thenReturn(DATA_OBJECT_URI);
		when(rdfDatabase.exists(databookResourceNoBracket("obj1"))).thenReturn(true);
		when(rdfDatabase.getUniqueValue(databookResourceNoBracket("obj1"), LABEL_URI)).thenReturn("/zone/dir/obj");
		when(rdfService.sparqlSelectQuery(contains(LABEL_URI))).thenAnswer(new Answer<InputStream>(){
			
			@Override
			public InputStream answer(InvocationOnMock invocation)
					throws Throwable {
				return (new ByteArrayInputStream("/zone/dir/obj\n".getBytes()));
			}
			
		});

		HashMap map = (HashMap) json().messages()
				.modify()
					.dataObject()
						.uri(databookResourceNoBracket("obj1"))
					.end()
					.dataObject()
						.label("/zone/dir/bar")
					.end()
			.end().run();
		
		String msg = new ObjectMapper().writeValueAsString(map);
		modelUpdater.update(msg);
		
		InOrder inOrder = inOrder(rdfDatabaseTransaction);
		inOrder.verify(rdfDatabaseTransaction).start();

		inOrder.verify(rdfDatabaseTransaction).remove(
				databookStatement(databookResource("obj1"), bracket(LABEL_URI), databookString("/zone/dir/obj")), 
				RDFDatabase.Format.N3, 
				LABEL_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).add(
				databookStatement(databookResource("obj1"), bracket(LABEL_URI), databookString("/zone/dir/bar")), 
				RDFDatabase.Format.N3, 
				LABEL_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).commit();
		
	}

	@Test
	public void testMoveDataObj() throws Exception  {
		when(rdfDatabase.getEntityTypeUri(databookResourceNoBracket("obj1"))).thenReturn(DATA_OBJECT_URI);
		when(rdfDatabase.exists(databookResourceNoBracket("obj1"))).thenReturn(true);
		when(rdfDatabase.getUniqueValue(databookResourceNoBracket("obj1"), LABEL_URI)).thenReturn("/zone/dir/obj");
		when(rdfService.sparqlSelectQuery(contains(LABEL_URI))).thenAnswer(new Answer<InputStream>(){
			
			@Override
			public InputStream answer(InvocationOnMock invocation)
					throws Throwable {
				return (new ByteArrayInputStream("/zone/dir/obj\n".getBytes()));
			}
			
		});
		
		HashMap map = (HashMap) json().messages()
				.diff()
					.collection()
						.uri(databookResourceNoBracket("col1"))
					.end()
					.collection()
						.hasPart()
							.dataObject().uri(databookResourceNoBracket("obj1")).end()
						.end()
					.end()
				.union()
					.collection()
						.uri(databookResourceNoBracket("hom1"))
					.end()
					.collection()
						.hasPart()
							.dataObject().uri(databookResourceNoBracket("obj1")).end()
						.end()
					.end()
				.modify()
					.dataObject()
						.uri(databookResourceNoBracket("obj1"))
					.end()
					.dataObject()
						.label("/zone/hom/obj")
					.end()
			.end().run();
		
		String msg = new ObjectMapper().writeValueAsString(map);

		
		modelUpdater.update(msg);
		
		InOrder inOrder = inOrder(rdfDatabaseTransaction);
		inOrder.verify(rdfDatabaseTransaction).start();
		inOrder.verify(rdfDatabaseTransaction).remove(
				databookStatement(databookResource("col1"), bracket(HAS_PART_URI), databookResource("obj1")), 
				RDFDatabase.Format.N3, 
				DATABOOK_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).add(
				databookStatement(databookResource("hom1"), bracket(HAS_PART_URI), databookResource("obj1")), 
				RDFDatabase.Format.N3, 
				DATABOOK_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).remove(
				databookStatement(databookResource("obj1"), bracket(LABEL_URI), databookString("/zone/dir/obj")), 
				RDFDatabase.Format.N3, 
				LABEL_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).add(
				databookStatement(databookResource("obj1"), bracket(LABEL_URI), databookString("/zone/hom/obj")), 
				RDFDatabase.Format.N3, 
				LABEL_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).commit();
		
	}

	@Test
	public void testAddMeta() throws Exception  {
		
		HashMap map = (HashMap) json().messages()
				.union()
					.dataObject()
						.uri(databookResourceNoBracket("obj1"))
					.end()
					.dataObject()
						.key("dataSize").value("1024")
					.end()
			.end().run();
		String msg = new ObjectMapper().writeValueAsString(map);

		modelUpdater.update(msg);
		
		InOrder inOrder = inOrder(rdfDatabaseTransaction);
		inOrder.verify(rdfDatabaseTransaction).start();
		inOrder.verify(rdfDatabaseTransaction).add(
				databookStatement(databookResource("obj1"), bracket(DATA_SIZE_URI), databookDecimal("1024")), 
				RDFDatabase.Format.N3, 
				DATABOOK_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).commit();
		
	}

	@Test
	public void testDelMeta() throws Exception  {
				when(rdfService.sparqlSelectQuery(contains(DATA_SIZE_URI))).thenAnswer(new Answer<InputStream>(){
					
					@Override
					public InputStream answer(InvocationOnMock invocation)
							throws Throwable {
						return (new ByteArrayInputStream("1024\n".getBytes()));
					}
					
				});
		HashMap map = (HashMap) json().messages()
				.diff()
					.dataObject()
						.uri(databookResourceNoBracket("obj1"))
					.end()
					.dataObject()
						.key("dataSize").value("1024")
					.end()
			.end().run();
		String msg = new ObjectMapper().writeValueAsString(map);
		
		modelUpdater.update(msg);
		
		InOrder inOrder = inOrder(rdfDatabaseTransaction);
		inOrder.verify(rdfDatabaseTransaction).start();
		inOrder.verify(rdfDatabaseTransaction).remove(
				databookStatement(databookResource("obj1"), bracket(DATA_SIZE_URI), databookDecimal("1024")), 
				RDFDatabase.Format.N3, 
				DATABOOK_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).commit();
		
	}

	@Test
	public void testModMeta() throws Exception  {
		when(rdfDatabase.getEntityTypeUri(databookResourceNoBracket("obj1"))).thenReturn(DATA_OBJECT_URI);
		when(rdfDatabase.getUniqueValue(databookResourceNoBracket("obj1"), databookResourceNoBracket("dataSize"))).thenReturn("1024");
		when(rdfService.sparqlSelectQuery(contains(DATA_SIZE_URI))).thenAnswer(new Answer<InputStream>(){
			
			@Override
			public InputStream answer(InvocationOnMock invocation)
					throws Throwable {
				return (new ByteArrayInputStream("1024\n".getBytes()));
			}
			
		});
		
		HashMap map = (HashMap) json().messages()
				.modify()
					.dataObject()
						.uri(databookResourceNoBracket("obj1"))
					.end()
					.dataObject()
						.key("dataSize").value("1010")
					.end()
			.end().run();
		
		String msg = new ObjectMapper().writeValueAsString(map);
		
		
		modelUpdater.update(msg);
		
		InOrder inOrder = inOrder(rdfDatabaseTransaction);
		inOrder.verify(rdfDatabaseTransaction).start();
		inOrder.verify(rdfDatabaseTransaction).remove(
				databookStatement(databookResource("obj1"), bracket(DATA_SIZE_URI), databookDecimal("1024")), 
				RDFDatabase.Format.N3, 
				DATABOOK_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).add(
				databookStatement(databookResource("obj1"), bracket(DATA_SIZE_URI), databookDecimal("1010")), 
				RDFDatabase.Format.N3, 
				DATABOOK_MODEL_URI
		);
		inOrder.verify(rdfDatabaseTransaction).commit();
		
	}
	
	@Test
	public void testAddAVU() throws Exception  {
		when(rdfDatabase.getEntityTypeUri(databookResourceNoBracket("obj1"))).thenReturn(DATA_OBJECT_URI);
		when(rdfDatabase.exists(databookResourceNoBracket("obj1"))).thenReturn(true);

		HashMap map = (HashMap) json().messages()
				.union()
					.dataObject()
						.uri(databookResourceNoBracket("obj1"))
					.end()
					.dataObject()
						.key("metadata").array()
							.attribute("a").value("b").unit("c")
						.end()
					.end()
			.end().run();
		
		String msg = new ObjectMapper().writeValueAsString(map);
		modelUpdater.update(msg);
		
		InOrder inOrder = inOrder(rdfDatabaseTransaction);
		inOrder.verify(rdfDatabaseTransaction).start();
		inOrder.verify(rdfDatabaseTransaction).add(
				endsWith(databookStatement("", IS_A, bracket(AVU_URI))), 
				eq(RDFDatabase.Format.N3), 
				eq(DATABOOK_MODEL_URI)
		);
		inOrder.verify(rdfDatabaseTransaction).add(
				startsWith(databookResource("obj1") + " " + bracket(METADATA_URI)), 
				eq(RDFDatabase.Format.N3), 
				eq(DATABOOK_MODEL_URI)
		);
		inOrder.verify(rdfDatabaseTransaction).commit();
		Mockito.verify(rdfDatabaseTransaction).add(
				endsWith(databookStatement("", bracket(ATTRIBUTE_URI), databookString("a"))), 
				eq(RDFDatabase.Format.N3), 
				eq(DATABOOK_MODEL_URI)
		);
		Mockito.verify(rdfDatabaseTransaction).add(
				endsWith(databookStatement("", bracket(VALUE_URI), databookString("b"))), 
				eq(RDFDatabase.Format.N3), 
				eq(DATABOOK_MODEL_URI)
		);
		Mockito.verify(rdfDatabaseTransaction).add(
				endsWith(databookStatement("", bracket(UNIT_URI), databookString("c"))), 
				eq(RDFDatabase.Format.N3), 
				eq(DATABOOK_MODEL_URI)
		);
		
	}
	
	@Test
	public void testDelAVU() throws Exception  {
		final String uri = databookResourceNoBracket("avu123");
		when(rdfDatabase.getEntityTypeUri(databookResourceNoBracket("obj1"))).thenReturn(DATA_OBJECT_URI);
		when(rdfDatabase.exists(databookResourceNoBracket("obj1"))).thenReturn(true);
		when(rdfService.sparqlSelectQuery(contains(""))).thenAnswer(new Answer<InputStream>(){

			@Override
			public InputStream answer(InvocationOnMock invocation)
					throws Throwable {
					return (new ByteArrayInputStream("".getBytes()));
						
				
			}
			
		});
		when(rdfService.sparqlSelectQuery(contains(
			"SELECT ?var0 WHERE { "+databookResource("obj1")+" "+databookResource("metadata")+" ?var0 .\n"+
			"?var0 "+databookResource("attribute")+" a .\n"+
			"?var0 "+databookResource("value")+" b .\n"+
			"?var0 "+databookResource("unit")+" c .\n"+
			" }"))).thenAnswer(new Answer<InputStream>(){

			@Override
			public InputStream answer(InvocationOnMock invocation)
					throws Throwable {
				
				return (new ByteArrayInputStream((uri+"\n").getBytes()));
				
			}
			
		});
		when(rdfService.sparqlSelectQuery(contains(
			"SELECT ?var0 WHERE { "+databookResource("avu123")+" "+databookResource("attribute")+" ?var0 .\n"+
			" }"))).thenAnswer(new Answer<InputStream>(){

			@Override
			public InputStream answer(InvocationOnMock invocation)
					throws Throwable {
				
				return (new ByteArrayInputStream(("a\n").getBytes()));
				
			}
			
		});
		when(rdfService.sparqlSelectQuery(contains(
			"SELECT ?var0 WHERE { "+databookResource("avu123")+" "+databookResource("value")+" ?var0 .\n"+
			" }"))).thenAnswer(new Answer<InputStream>(){

			@Override
			public InputStream answer(InvocationOnMock invocation)
					throws Throwable {
				
				return (new ByteArrayInputStream(("b\n").getBytes()));
				
			}
			
		});
		when(rdfService.sparqlSelectQuery(contains(
			"SELECT ?var0 WHERE { "+databookResource("avu123")+" "+databookResource("unit")+" ?var0 .\n"+
			" }"))).thenAnswer(new Answer<InputStream>(){

			@Override
			public InputStream answer(InvocationOnMock invocation)
					throws Throwable {
				
				return (new ByteArrayInputStream(("c\n").getBytes()));
				
			}
			
		});
		HashMap map = (HashMap) json().messages()
				.diff()
					.dataObject()
						.uri(databookResourceNoBracket("obj1"))
					.end()
					.dataObject()
						.key("metadata").array()
							.attribute("a").value("b").unit("c")
						.end()
					.end()
			.end().run();

		
		String msg = new ObjectMapper().writeValueAsString(map);
		modelUpdater.update(msg);
		
		InOrder inOrder = inOrder(rdfDatabaseTransaction);
		inOrder.verify(rdfDatabaseTransaction).start();
		inOrder.verify(rdfDatabaseTransaction).remove(
				startsWith(databookResource("obj1") + " " + bracket(METADATA_URI)), 
				eq(RDFDatabase.Format.N3), 
				eq(DATABOOK_MODEL_URI)
		);
		inOrder.verify(rdfDatabaseTransaction).remove(
				endsWith(databookStatement("", IS_A, bracket(AVU_URI))), 
				eq(RDFDatabase.Format.N3), 
				eq(DATABOOK_MODEL_URI)
		);
		inOrder.verify(rdfDatabaseTransaction).commit();
		Mockito.verify(rdfDatabaseTransaction).remove(
				endsWith(databookStatement("", bracket(ATTRIBUTE_URI), databookString("a"))), 
				eq(RDFDatabase.Format.N3), 
				eq(DATABOOK_MODEL_URI)
		);
		Mockito.verify(rdfDatabaseTransaction).remove(
				endsWith(databookStatement("", bracket(VALUE_URI), databookString("b"))), 
				eq(RDFDatabase.Format.N3), 
				eq(DATABOOK_MODEL_URI)
		);
		Mockito.verify(rdfDatabaseTransaction).remove(
				endsWith(databookStatement("", bracket(UNIT_URI), databookString("c"))), 
				eq(RDFDatabase.Format.N3), 
				eq(DATABOOK_MODEL_URI)
		);
		
	}
	@Test
	public void testModAVU() throws Exception {
		String avuId = ModelUpdater.genAVUId("obj1", "a", "b", "c");
		when(rdfService.sparqlSelectQuery(contains(""))).thenAnswer(new Answer<InputStream>(){

			@Override
			public InputStream answer(InvocationOnMock invocation)
					throws Throwable {
					return (new ByteArrayInputStream("".getBytes()));
						
				
			}
			
		});
		when(rdfService.sparqlSelectQuery(contains(
			"SELECT ?var0 WHERE { "+databookResource("obj1")+" "+databookResource("metadata")+" ?var0 .\n"+
			"?var0 "+databookResource("attribute")+" a .\n"+
			"?var0 "+databookResource("value")+" b .\n"+
			"?var0 "+databookResource("unit")+" c .\n"+
			" }"))).thenAnswer(new Answer<InputStream>(){

			@Override
			public InputStream answer(InvocationOnMock invocation)
					throws Throwable {
				
				return (new ByteArrayInputStream((databookResourceNoBracket("avu123")+"\n").getBytes()));
				
			}
			
		});
		when(rdfService.sparqlSelectQuery(contains(
			"SELECT ?var0 WHERE { "+databookResource("avu123")+" "+databookResource("attribute")+" ?var0 .\n"+
			" }"))).thenAnswer(new Answer<InputStream>(){

			@Override
			public InputStream answer(InvocationOnMock invocation)
					throws Throwable {
				
				return (new ByteArrayInputStream(("a\n").getBytes()));
				
			}
			
		});
		when(rdfService.sparqlSelectQuery(contains(
			"SELECT ?var0 WHERE { "+databookResource("avu123")+" "+databookResource("value")+" ?var0 .\n"+
			" }"))).thenAnswer(new Answer<InputStream>(){

			@Override
			public InputStream answer(InvocationOnMock invocation)
					throws Throwable {
				
				return (new ByteArrayInputStream(("b\n").getBytes()));
				
			}
			
		});
		when(rdfService.sparqlSelectQuery(contains(
			"SELECT ?var0 WHERE { "+databookResource("avu123")+" "+databookResource("unit")+" ?var0 .\n"+
			" }"))).thenAnswer(new Answer<InputStream>(){

			@Override
			public InputStream answer(InvocationOnMock invocation)
					throws Throwable {
				
				return (new ByteArrayInputStream(("c\n").getBytes()));
				
			}
			
		});

		HashMap map = (HashMap) json().messages()
				.modify()
					.dataObject()
						.uri(databookResourceNoBracket("obj1"))
						.key("metadata").array()
							.attribute("a").value("b").unit("c")
						.end()
					.end()
					.dataObject()
						.key("metadata").array()
							.attribute("a1").value("b1").unit("c1")
						.end()
					.end()
			.end().run();
		String msg = new ObjectMapper().writeValueAsString(map);

		modelUpdater.update(msg);
		
		InOrder inOrder = inOrder(rdfDatabaseTransaction);
		inOrder.verify(rdfDatabaseTransaction).start();
		inOrder.verify(rdfDatabaseTransaction).remove(
				startsWith(databookResource("obj1") + " " + bracket(METADATA_URI)), 
				eq(RDFDatabase.Format.N3), 
				eq(DATABOOK_MODEL_URI)
		);
		inOrder.verify(rdfDatabaseTransaction).remove(
				endsWith(databookStatement("", IS_A, bracket(AVU_URI))), 
				eq(RDFDatabase.Format.N3), 
				eq(DATABOOK_MODEL_URI)
		);
		inOrder.verify(rdfDatabaseTransaction).add(
				endsWith(databookStatement("", IS_A, bracket(AVU_URI))), 
				eq(RDFDatabase.Format.N3), 
				eq(DATABOOK_MODEL_URI)
		);
		inOrder.verify(rdfDatabaseTransaction).add(
				startsWith(databookResource("obj1") + " " + bracket(METADATA_URI)), 
				eq(RDFDatabase.Format.N3), 
				eq(DATABOOK_MODEL_URI)
		);
		inOrder.verify(rdfDatabaseTransaction).commit();
		Mockito.verify(rdfDatabaseTransaction).remove(
				endsWith(databookStatement("", bracket(ATTRIBUTE_URI), databookString("a"))), 
				eq(RDFDatabase.Format.N3), 
				eq(DATABOOK_MODEL_URI)
		);
		Mockito.verify(rdfDatabaseTransaction).remove(
				endsWith(databookStatement("", bracket(VALUE_URI), databookString("b"))), 
				eq(RDFDatabase.Format.N3), 
				eq(DATABOOK_MODEL_URI)
		);
		Mockito.verify(rdfDatabaseTransaction).remove(
				endsWith(databookStatement("", bracket(UNIT_URI), databookString("c"))), 
				eq(RDFDatabase.Format.N3), 
				eq(DATABOOK_MODEL_URI)
		);
		Mockito.verify(rdfDatabaseTransaction).add(
				endsWith(databookStatement("", bracket(ATTRIBUTE_URI), databookString("a1"))), 
				eq(RDFDatabase.Format.N3), 
				eq(DATABOOK_MODEL_URI)
		);
		Mockito.verify(rdfDatabaseTransaction).add(
				endsWith(databookStatement("", bracket(VALUE_URI), databookString("b1"))), 
				eq(RDFDatabase.Format.N3), 
				eq(DATABOOK_MODEL_URI)
		);
		Mockito.verify(rdfDatabaseTransaction).add(
				endsWith(databookStatement("", bracket(UNIT_URI), databookString("c1"))), 
				eq(RDFDatabase.Format.N3), 
				eq(DATABOOK_MODEL_URI)
		);
	}
}
