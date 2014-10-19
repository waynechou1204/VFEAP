package dataStructure;

import java.io.OutputStream;

import jsdai.lang.ASdaiModel;
import jsdai.lang.ASdaiRepository;
import jsdai.lang.A_string;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;
import jsdai.lang.SdaiTransaction;
import jsdai.xml.EarlyBindingV2Reader;
import jsdai.xml.InstanceReader;

public class VFIFE_repository {

    private SdaiSession m_session;
    private SdaiTransaction m_transaction;
    private SdaiRepository m_repo = null;
    private SdaiModel m_model;

    // constructor
    public VFIFE_repository(String propKey, String propValuePath) throws SdaiException {

        java.util.Properties prop = new java.util.Properties();
        prop.setProperty(propKey, propValuePath);

        SdaiSession.setSessionProperties(prop);

        m_session = SdaiSession.openSession();
        m_transaction = m_session.startTransactionReadWriteAccess();
    }

    // load stp file into the repo
    public SdaiModel loadFile(String repoName, String filepath) throws SdaiException {
        try {
            ASdaiRepository repos = m_session.getKnownServers();
            SdaiIterator repoiter = repos.createIterator();

            while (repoiter.next()) {
                m_repo = repos.getCurrentMember(repoiter);
                if (m_repo.getName().equals(repoName)) {
                    break;
                }
            }

            // import stp file
            if (!m_repo.getName().equals(repoName)) {
                m_repo = m_session.importClearTextEncoding(repoName, filepath, null);
            }

            if (!m_repo.isActive()) {
                m_repo.openRepository();
            }

            m_transaction.commit();

            // deal model
            ASdaiModel models = m_repo.getModels();
            SdaiIterator modeliter = models.createIterator();
            while (modeliter.next()) {
                m_model = models.getCurrentMember(modeliter);

                if (m_model.getMode() == SdaiModel.NO_ACCESS) {
                    m_model.startReadOnlyAccess();
                    return m_model;
                }
            }
        } catch (Exception e) {
            m_repo.deleteRepository();
        }
        return m_model;
    }

    // set schema type to the model
    public SdaiModel setVfifeOutModel(String repoName, String modelname)
            throws SdaiException {
        ASdaiRepository repos = m_session.getKnownServers();
        SdaiIterator repoiter = repos.createIterator();
        while (repoiter.next()) {
            m_repo = repos.getCurrentMember(repoiter);
            if (m_repo.getName().equals(repoName)) {
                break;
            }
        }

        // if not exist, create a repo
        if (!m_repo.getName().equals(repoName)) {
            m_repo = m_session.createRepository(repoName, null);
        }

        if (!m_repo.isActive()) {
            m_repo.openRepository();
        }

        // add description
        A_string descriptions = m_repo.getDescription();
        descriptions.addByIndex(1,
                "VFIFE Analysis generates a model file with STP format");

        // add author
        A_string authors = m_repo.getAuthor();
        authors.addByIndex(1, "Wayne Chou");

        // add organization
        A_string organizations = m_repo.getOrganization();
        organizations.addByIndex(1, "SSE, Tongji");

        // set originating system
        m_repo.setOriginatingSystem(m_session.getSdaiImplementation().getName()
                + " " + m_session.getSdaiImplementation().getLevel());

        // set authorization
        m_repo.setAuthorization("Xiaowei authorize");

        // create a model
        m_model = m_repo.createSdaiModel(modelname,
                jsdai.SVfife_schema.SVfife_schema.class);
        m_model.startReadWriteAccess();
        return m_model;
    }

    // write out the repo to a stp
    public void outputFile(String filepath) throws SdaiException {
        if (m_repo != null) {
            m_repo.exportClearTextEncoding(filepath);
        }
    }

    // write out the repo to a xml
    public void outputFile(OutputStream outstream) throws SdaiException {
        if (m_repo != null) {
            InstanceReader reader = new EarlyBindingV2Reader();
            m_repo.exportXml(outstream, reader);
        }
    }

    // close the repo
    public void close() throws SdaiException {
        m_transaction.endTransactionAccessAbort();
        m_repo.closeRepository();
        m_repo.deleteRepository();
        m_session.closeSession();
    }

}
