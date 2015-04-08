
package com.exlibris.dps;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.exlibris.dps package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetHeartBit_QNAME = new QName("http://dps.exlibris.com/", "getHeartBit");
    private final static QName _UpdateSetMembers_QNAME = new QName("http://dps.exlibris.com/", "updateSetMembers");
    private final static QName _UserAuthorizeException_QNAME = new QName("http://dps.exlibris.com/", "UserAuthorizeException");
    private final static QName _RunProcessResponse_QNAME = new QName("http://dps.exlibris.com/", "runProcessResponse");
    private final static QName _GetProcessExecutionStatusResponse_QNAME = new QName("http://dps.exlibris.com/", "getProcessExecutionStatusResponse");
    private final static QName _SetNotValidException_QNAME = new QName("http://dps.exlibris.com/", "SetNotValidException");
    private final static QName _ProcessException_QNAME = new QName("http://dps.exlibris.com/", "ProcessException");
    private final static QName _GetHeartBitResponse_QNAME = new QName("http://dps.exlibris.com/", "getHeartBitResponse");
    private final static QName _UpdateSetMembersResponse_QNAME = new QName("http://dps.exlibris.com/", "updateSetMembersResponse");
    private final static QName _GetProcessExecutionStatus_QNAME = new QName("http://dps.exlibris.com/", "getProcessExecutionStatus");
    private final static QName _RunProcess_QNAME = new QName("http://dps.exlibris.com/", "runProcess");

    
    private final static QName _CreateCollectionResponse_QNAME = new QName("http://dps.exlibris.com/", "createCollectionResponse");
    private final static QName _GetCollectionByName_QNAME = new QName("http://dps.exlibris.com/", "getCollectionByName");
    private final static QName _NoCollectionFoundException_QNAME = new QName("http://dps.exlibris.com/", "NoCollectionFoundException");
    private final static QName _GetCollectionById_QNAME = new QName("http://dps.exlibris.com/", "getCollectionById");
    private final static QName _CreateCollection_QNAME = new QName("http://dps.exlibris.com/", "createCollection");
    private final static QName _InvalidXmlException_QNAME = new QName("http://dps.exlibris.com/", "InvalidXmlException");
    private final static QName _DeleteCollectionException_QNAME = new QName("http://dps.exlibris.com/", "DeleteCollectionException");
    private final static QName _GetCollectionByExternalId_QNAME = new QName("http://dps.exlibris.com/", "getCollectionByExternalId");
    private final static QName _GetCollectionByNameResponse_QNAME = new QName("http://dps.exlibris.com/", "getCollectionByNameResponse");
    private final static QName _InvalidTypeException_QNAME = new QName("http://dps.exlibris.com/", "InvalidTypeException");
    private final static QName _UpdateCollection_QNAME = new QName("http://dps.exlibris.com/", "updateCollection");
    private final static QName _UpdateCollectionResponse_QNAME = new QName("http://dps.exlibris.com/", "updateCollectionResponse");
    private final static QName _Exception_QNAME = new QName("http://dps.exlibris.com/", "Exception");
    private final static QName _DeleteCollection_QNAME = new QName("http://dps.exlibris.com/", "deleteCollection");
    private final static QName _InvalidCollectionInfoException_QNAME = new QName("http://dps.exlibris.com/", "InvalidCollectionInfoException");
    private final static QName _DeleteCollectionResponse_QNAME = new QName("http://dps.exlibris.com/", "deleteCollectionResponse");
    private final static QName _GetCollectionByIdResponse_QNAME = new QName("http://dps.exlibris.com/", "getCollectionByIdResponse");
    private final static QName _GetCollectionByExternalIdResponse_QNAME = new QName("http://dps.exlibris.com/", "getCollectionByExternalIdResponse");
    private final static QName _InvalidMIDException_QNAME = new QName("http://dps.exlibris.com/", "InvalidMIDException");
    private final static QName _InstitutionAccessException_QNAME = new QName("http://dps.exlibris.com/", "InstitutionAccessException");
    
    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.exlibris.dps
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RunProcessResponse }
     * 
     */
    public RunProcessResponse createRunProcessResponse() {
        return new RunProcessResponse();
    }

    /**
     * Create an instance of {@link SetMembersInfo }
     * 
     */
    public SetMembersInfo createSetMembersInfo() {
        return new SetMembersInfo();
    }

    /**
     * Create an instance of {@link SetNotValidException }
     * 
     */
    public SetNotValidException createSetNotValidException() {
        return new SetNotValidException();
    }

    /**
     * Create an instance of {@link ProcessException }
     * 
     */
    public ProcessException createProcessException() {
        return new ProcessException();
    }

    /**
     * Create an instance of {@link RunProcess }
     * 
     */
    public RunProcess createRunProcess() {
        return new RunProcess();
    }

    /**
     * Create an instance of {@link GetProcessExecutionStatusResponse }
     * 
     */
    public GetProcessExecutionStatusResponse createGetProcessExecutionStatusResponse() {
        return new GetProcessExecutionStatusResponse();
    }

    /**
     * Create an instance of {@link UpdateSetMembers }
     * 
     */
    public UpdateSetMembers createUpdateSetMembers() {
        return new UpdateSetMembers();
    }


    /**
     * Create an instance of {@link GetProcessExecutionStatus }
     * 
     */
    public GetProcessExecutionStatus createGetProcessExecutionStatus() {
        return new GetProcessExecutionStatus();
    }

    /**
     * Create an instance of {@link ProcessExecutionStatusInfo }
     * 
     */
    public ProcessExecutionStatusInfo createProcessExecutionStatusInfo() {
        return new ProcessExecutionStatusInfo();
    }

    /**
     * Create an instance of {@link UpdateSetMembersResponse }
     * 
     */
    public UpdateSetMembersResponse createUpdateSetMembersResponse() {
        return new UpdateSetMembersResponse();
    }

    /**
     * Create an instance of {@link PaExecutionError }
     * 
     */
    public PaExecutionError createPaExecutionError() {
        return new PaExecutionError();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateSetMembers }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "updateSetMembers")
    public JAXBElement<UpdateSetMembers> createUpdateSetMembers(UpdateSetMembers value) {
        return new JAXBElement<UpdateSetMembers>(_UpdateSetMembers_QNAME, UpdateSetMembers.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RunProcessResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "runProcessResponse")
    public JAXBElement<RunProcessResponse> createRunProcessResponse(RunProcessResponse value) {
        return new JAXBElement<RunProcessResponse>(_RunProcessResponse_QNAME, RunProcessResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetProcessExecutionStatusResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "getProcessExecutionStatusResponse")
    public JAXBElement<GetProcessExecutionStatusResponse> createGetProcessExecutionStatusResponse(GetProcessExecutionStatusResponse value) {
        return new JAXBElement<GetProcessExecutionStatusResponse>(_GetProcessExecutionStatusResponse_QNAME, GetProcessExecutionStatusResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetNotValidException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "SetNotValidException")
    public JAXBElement<SetNotValidException> createSetNotValidException(SetNotValidException value) {
        return new JAXBElement<SetNotValidException>(_SetNotValidException_QNAME, SetNotValidException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "ProcessException")
    public JAXBElement<ProcessException> createProcessException(ProcessException value) {
        return new JAXBElement<ProcessException>(_ProcessException_QNAME, ProcessException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateSetMembersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "updateSetMembersResponse")
    public JAXBElement<UpdateSetMembersResponse> createUpdateSetMembersResponse(UpdateSetMembersResponse value) {
        return new JAXBElement<UpdateSetMembersResponse>(_UpdateSetMembersResponse_QNAME, UpdateSetMembersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetProcessExecutionStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "getProcessExecutionStatus")
    public JAXBElement<GetProcessExecutionStatus> createGetProcessExecutionStatus(GetProcessExecutionStatus value) {
        return new JAXBElement<GetProcessExecutionStatus>(_GetProcessExecutionStatus_QNAME, GetProcessExecutionStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RunProcess }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "runProcess")
    public JAXBElement<RunProcess> createRunProcess(RunProcess value) {
        return new JAXBElement<RunProcess>(_RunProcess_QNAME, RunProcess.class, null, value);
    }

    /**
     * Create an instance of {@link UpdateCollectionResponse }
     * 
     */
    public UpdateCollectionResponse createUpdateCollectionResponse() {
        return new UpdateCollectionResponse();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link DeleteCollection }
     * 
     */
    public DeleteCollection createDeleteCollection() {
        return new DeleteCollection();
    }

    /**
     * Create an instance of {@link InvalidCollectionInfoException }
     * 
     */
    public InvalidCollectionInfoException createInvalidCollectionInfoException() {
        return new InvalidCollectionInfoException();
    }

    /**
     * Create an instance of {@link DeleteCollectionResponse }
     * 
     */
    public DeleteCollectionResponse createDeleteCollectionResponse() {
        return new DeleteCollectionResponse();
    }

    /**
     * Create an instance of {@link GetCollectionByIdResponse }
     * 
     */
    public GetCollectionByIdResponse createGetCollectionByIdResponse() {
        return new GetCollectionByIdResponse();
    }

    /**
     * Create an instance of {@link GetHeartBit }
     * 
     */
    public GetHeartBit createGetHeartBit() {
        return new GetHeartBit();
    }

    /**
     * Create an instance of {@link UpdateCollection }
     * 
     */
    public UpdateCollection createUpdateCollection() {
        return new UpdateCollection();
    }

    /**
     * Create an instance of {@link UserAuthorizeException }
     * 
     */
    public UserAuthorizeException createUserAuthorizeException() {
        return new UserAuthorizeException();
    }

    /**
     * Create an instance of {@link InstitutionAccessException }
     * 
     */
    public InstitutionAccessException createInstitutionAccessException() {
        return new InstitutionAccessException();
    }

    /**
     * Create an instance of {@link GetCollectionByExternalIdResponse }
     * 
     */
    public GetCollectionByExternalIdResponse createGetCollectionByExternalIdResponse() {
        return new GetCollectionByExternalIdResponse();
    }

    /**
     * Create an instance of {@link InvalidMIDException }
     * 
     */
    public InvalidMIDException createInvalidMIDException() {
        return new InvalidMIDException();
    }

    /**
     * Create an instance of {@link GetCollectionById }
     * 
     */
    public GetCollectionById createGetCollectionById() {
        return new GetCollectionById();
    }

    /**
     * Create an instance of {@link CreateCollection }
     * 
     */
    public CreateCollection createCreateCollection() {
        return new CreateCollection();
    }

    /**
     * Create an instance of {@link CreateCollectionResponse }
     * 
     */
    public CreateCollectionResponse createCreateCollectionResponse() {
        return new CreateCollectionResponse();
    }

    /**
     * Create an instance of {@link GetCollectionByName }
     * 
     */
    public GetCollectionByName createGetCollectionByName() {
        return new GetCollectionByName();
    }

    /**
     * Create an instance of {@link NoCollectionFoundException }
     * 
     */
    public NoCollectionFoundException createNoCollectionFoundException() {
        return new NoCollectionFoundException();
    }

    /**
     * Create an instance of {@link GetHeartBitResponse }
     * 
     */
    public GetHeartBitResponse createGetHeartBitResponse() {
        return new GetHeartBitResponse();
    }

    /**
     * Create an instance of {@link GetCollectionByNameResponse }
     * 
     */
    public GetCollectionByNameResponse createGetCollectionByNameResponse() {
        return new GetCollectionByNameResponse();
    }

    /**
     * Create an instance of {@link InvalidTypeException }
     * 
     */
    public InvalidTypeException createInvalidTypeException() {
        return new InvalidTypeException();
    }

    /**
     * Create an instance of {@link InvalidXmlException }
     * 
     */
    public InvalidXmlException createInvalidXmlException() {
        return new InvalidXmlException();
    }

    /**
     * Create an instance of {@link DeleteCollectionException }
     * 
     */
    public DeleteCollectionException createDeleteCollectionException() {
        return new DeleteCollectionException();
    }

    /**
     * Create an instance of {@link GetCollectionByExternalId }
     * 
     */
    public GetCollectionByExternalId createGetCollectionByExternalId() {
        return new GetCollectionByExternalId();
    }

    /**
     * Create an instance of {@link Collection }
     * 
     */
    public Collection createCollection() {
        return new Collection();
    }

    /**
     * Create an instance of {@link MetaData }
     * 
     */
    public MetaData createMetaData() {
        return new MetaData();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateCollectionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "createCollectionResponse")
    public JAXBElement<CreateCollectionResponse> createCreateCollectionResponse(CreateCollectionResponse value) {
        return new JAXBElement<CreateCollectionResponse>(_CreateCollectionResponse_QNAME, CreateCollectionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCollectionByName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "getCollectionByName")
    public JAXBElement<GetCollectionByName> createGetCollectionByName(GetCollectionByName value) {
        return new JAXBElement<GetCollectionByName>(_GetCollectionByName_QNAME, GetCollectionByName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NoCollectionFoundException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "NoCollectionFoundException")
    public JAXBElement<NoCollectionFoundException> createNoCollectionFoundException(NoCollectionFoundException value) {
        return new JAXBElement<NoCollectionFoundException>(_NoCollectionFoundException_QNAME, NoCollectionFoundException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCollectionById }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "getCollectionById")
    public JAXBElement<GetCollectionById> createGetCollectionById(GetCollectionById value) {
        return new JAXBElement<GetCollectionById>(_GetCollectionById_QNAME, GetCollectionById.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateCollection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "createCollection")
    public JAXBElement<CreateCollection> createCreateCollection(CreateCollection value) {
        return new JAXBElement<CreateCollection>(_CreateCollection_QNAME, CreateCollection.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InvalidXmlException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "InvalidXmlException")
    public JAXBElement<InvalidXmlException> createInvalidXmlException(InvalidXmlException value) {
        return new JAXBElement<InvalidXmlException>(_InvalidXmlException_QNAME, InvalidXmlException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteCollectionException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "DeleteCollectionException")
    public JAXBElement<DeleteCollectionException> createDeleteCollectionException(DeleteCollectionException value) {
        return new JAXBElement<DeleteCollectionException>(_DeleteCollectionException_QNAME, DeleteCollectionException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCollectionByExternalId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "getCollectionByExternalId")
    public JAXBElement<GetCollectionByExternalId> createGetCollectionByExternalId(GetCollectionByExternalId value) {
        return new JAXBElement<GetCollectionByExternalId>(_GetCollectionByExternalId_QNAME, GetCollectionByExternalId.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCollectionByNameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "getCollectionByNameResponse")
    public JAXBElement<GetCollectionByNameResponse> createGetCollectionByNameResponse(GetCollectionByNameResponse value) {
        return new JAXBElement<GetCollectionByNameResponse>(_GetCollectionByNameResponse_QNAME, GetCollectionByNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetHeartBitResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "getHeartBitResponse")
    public JAXBElement<GetHeartBitResponse> createGetHeartBitResponse(GetHeartBitResponse value) {
        return new JAXBElement<GetHeartBitResponse>(_GetHeartBitResponse_QNAME, GetHeartBitResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InvalidTypeException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "InvalidTypeException")
    public JAXBElement<InvalidTypeException> createInvalidTypeException(InvalidTypeException value) {
        return new JAXBElement<InvalidTypeException>(_InvalidTypeException_QNAME, InvalidTypeException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetHeartBit }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "getHeartBit")
    public JAXBElement<GetHeartBit> createGetHeartBit(GetHeartBit value) {
        return new JAXBElement<GetHeartBit>(_GetHeartBit_QNAME, GetHeartBit.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateCollection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "updateCollection")
    public JAXBElement<UpdateCollection> createUpdateCollection(UpdateCollection value) {
        return new JAXBElement<UpdateCollection>(_UpdateCollection_QNAME, UpdateCollection.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserAuthorizeException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "UserAuthorizeException")
    public JAXBElement<UserAuthorizeException> createUserAuthorizeException(UserAuthorizeException value) {
        return new JAXBElement<UserAuthorizeException>(_UserAuthorizeException_QNAME, UserAuthorizeException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateCollectionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "updateCollectionResponse")
    public JAXBElement<UpdateCollectionResponse> createUpdateCollectionResponse(UpdateCollectionResponse value) {
        return new JAXBElement<UpdateCollectionResponse>(_UpdateCollectionResponse_QNAME, UpdateCollectionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteCollection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "deleteCollection")
    public JAXBElement<DeleteCollection> createDeleteCollection(DeleteCollection value) {
        return new JAXBElement<DeleteCollection>(_DeleteCollection_QNAME, DeleteCollection.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InvalidCollectionInfoException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "InvalidCollectionInfoException")
    public JAXBElement<InvalidCollectionInfoException> createInvalidCollectionInfoException(InvalidCollectionInfoException value) {
        return new JAXBElement<InvalidCollectionInfoException>(_InvalidCollectionInfoException_QNAME, InvalidCollectionInfoException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteCollectionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "deleteCollectionResponse")
    public JAXBElement<DeleteCollectionResponse> createDeleteCollectionResponse(DeleteCollectionResponse value) {
        return new JAXBElement<DeleteCollectionResponse>(_DeleteCollectionResponse_QNAME, DeleteCollectionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCollectionByIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "getCollectionByIdResponse")
    public JAXBElement<GetCollectionByIdResponse> createGetCollectionByIdResponse(GetCollectionByIdResponse value) {
        return new JAXBElement<GetCollectionByIdResponse>(_GetCollectionByIdResponse_QNAME, GetCollectionByIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCollectionByExternalIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "getCollectionByExternalIdResponse")
    public JAXBElement<GetCollectionByExternalIdResponse> createGetCollectionByExternalIdResponse(GetCollectionByExternalIdResponse value) {
        return new JAXBElement<GetCollectionByExternalIdResponse>(_GetCollectionByExternalIdResponse_QNAME, GetCollectionByExternalIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InvalidMIDException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "InvalidMIDException")
    public JAXBElement<InvalidMIDException> createInvalidMIDException(InvalidMIDException value) {
        return new JAXBElement<InvalidMIDException>(_InvalidMIDException_QNAME, InvalidMIDException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InstitutionAccessException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dps.exlibris.com/", name = "InstitutionAccessException")
    public JAXBElement<InstitutionAccessException> createInstitutionAccessException(InstitutionAccessException value) {
        return new JAXBElement<InstitutionAccessException>(_InstitutionAccessException_QNAME, InstitutionAccessException.class, null, value);
    }
    
}
