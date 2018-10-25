package uk.ac.ebi.subs.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.subs.repository.model.Study;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SerializationTest {

    @Autowired
    private ObjectMapper om;

    private String studyJson;

    @Test
    public void test() throws IOException {
        JsonNode node = om.readTree(studyJson);
        Study study = om.treeToValue(node,Study.class);

    }

    @Before
    public void before(){
        studyJson = "{\n" +
                "  \"id\": \"ecbe9339-0e21-4a36-af53-c04f605e1aaf\",\n" +
                "  \"alias\": \"alias-254432\",\n" +
                "  \"team\": {\n" +
                "    \"name\": \"subs.api-tester-team-1\",\n" +
                "    \"description\": \"api tester account\",\n" +
                "    \"profile\": {\n" +
                "      \"centre name\": \"EMBL-EBI\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"title\": \"Study title\",\n" +
                "  \"description\": \"Description\",\n" +
                "  \"attributes\": {\n" +
                "    \"study_type\": [\n" +
                "      {\n" +
                "        \"value\": \"Whole Genome Sequencing\",\n" +
                "        \"terms\": [\n" +
                "\n" +
                "        ]\n" +
                "      }\n" +
                "    ],\n" +
                "    \"Project Coordinator\": [\n" +
                "      {\n" +
                "        \"value\": \"John Doe\",\n" +
                "        \"terms\": [\n" +
                "\n" +
                "        ]\n" +
                "      }\n" +
                "    ],\n" +
                "    \"study_abstract\": [\n" +
                "      {\n" +
                "        \"value\": \"study abstract\",\n" +
                "        \"terms\": [\n" +
                "\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"projectRef\": {\n" +
                "    \"alias\": \"alias-193324\",\n" +
                "    \"team\": \"subs.api-tester-team-1\"\n" +
                "  },\n" +
                "  \"processingStatus\": {\n" +
                "    \"status\": \"Draft\",\n" +
                "    \"id\": \"a15777c2-0cf2-4451-9e88-4765ce844643\",\n" +
                "    \"version\": 0,\n" +
                "    \"createdDate\": 1540471298121,\n" +
                "    \"lastModifiedDate\": 1540471298121,\n" +
                "    \"createdBy\": \"usr-8623f88e-4f08-4995-995e-0495f53bdff8\",\n" +
                "    \"lastModifiedBy\": \"usr-8623f88e-4f08-4995-995e-0495f53bdff8\",\n" +
                "    \"submissionId\": \"ea6131bf-4add-4794-8910-0f34f6f626ee\",\n" +
                "    \"submittableId\": \"ecbe9339-0e21-4a36-af53-c04f605e1aaf\",\n" +
                "    \"submittableType\": \"Study\",\n" +
                "    \"alias\": \"alias-254432\"\n" +
                "  },\n" +
                "  \"version\": 2,\n" +
                "  \"createdDate\": 1540471298097,\n" +
                "  \"lastModifiedDate\": 1540471298127,\n" +
                "  \"createdBy\": \"usr-8623f88e-4f08-4995-995e-0495f53bdff8\",\n" +
                "  \"lastModifiedBy\": \"usr-8623f88e-4f08-4995-995e-0495f53bdff8\",\n" +
                "  \"submission\": {\n" +
                "    \"id\": \"ea6131bf-4add-4794-8910-0f34f6f626ee\",\n" +
                "    \"submitter\": {\n" +
                "      \"email\": \"subs-internal@ebi.ac.uk\",\n" +
                "      \"name\": \"USI submission test account\"\n" +
                "    },\n" +
                "    \"team\": {\n" +
                "      \"name\": \"subs.api-tester-team-1\",\n" +
                "      \"description\": \"api tester account\",\n" +
                "      \"profile\": {\n" +
                "        \"centre name\": \"EMBL-EBI\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"submissionStatus\": {\n" +
                "      \"status\": \"Draft\",\n" +
                "      \"id\": \"b05ed3b3-4331-4d4e-8426-eacd97fa160e\",\n" +
                "      \"version\": 0,\n" +
                "      \"createdDate\": 1540471297871,\n" +
                "      \"lastModifiedDate\": 1540471297871,\n" +
                "      \"createdBy\": \"usr-8623f88e-4f08-4995-995e-0495f53bdff8\",\n" +
                "      \"lastModifiedBy\": \"usr-8623f88e-4f08-4995-995e-0495f53bdff8\",\n" +
                "      \"team\": {\n" +
                "        \"name\": \"subs.api-tester-team-1\",\n" +
                "        \"description\": \"api tester account\",\n" +
                "        \"profile\": {\n" +
                "          \"centre name\": \"EMBL-EBI\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"version\": 0,\n" +
                "    \"createdDate\": 1540471297890,\n" +
                "    \"lastModifiedDate\": 1540471297890,\n" +
                "    \"createdBy\": \"usr-8623f88e-4f08-4995-995e-0495f53bdff8\",\n" +
                "    \"lastModifiedBy\": \"usr-8623f88e-4f08-4995-995e-0495f53bdff8\"\n" +
                "  },\n" +
                "  \"validationResult\": {\n" +
                "    \"uuid\": \"18ee5be9-923c-476c-af16-8d96b4812516\",\n" +
                "    \"version\": 1,\n" +
                "    \"entityType\": \"uk.ac.ebi.subs.repository.model.Study\",\n" +
                "    \"entityUuid\": \"ecbe9339-0e21-4a36-af53-c04f605e1aaf\",\n" +
                "    \"dataTypeId\": \"enaStudies\",\n" +
                "    \"validationStatus\": \"Pending\",\n" +
                "    \"submissionId\": \"ea6131bf-4add-4794-8910-0f34f6f626ee\",\n" +
                "    \"expectedResults\": {\n" +
                "      \"Core\": [\n" +
                "\n" +
                "      ],\n" +
                "      \"JsonSchema\": [\n" +
                "\n" +
                "      ],\n" +
                "      \"Ena\": [\n" +
                "\n" +
                "      ]\n" +
                "    }\n" +
                "  },\n" +
                "  \"dataType\": {\n" +
                "    \"id\": \"enaStudies\",\n" +
                "    \"displayNameSingular\": \"ENA/EVA Study\",\n" +
                "    \"displayNamePlural\": \"ENA/EVA Studies\",\n" +
                "    \"description\": \"A description of a scientific study to be archived by the ENA or EVA. It forms part of a project.\",\n" +
                "    \"refRequirements\": [\n" +
                "      {\n" +
                "        \"refClassName\": \"uk.ac.ebi.subs.data.component.ProjectRef\",\n" +
                "        \"dataTypeIdForReferencedDocument\": \"projects\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"requiredValidationAuthors\": [\n" +
                "      \"Ena\",\n" +
                "      \"Core\",\n" +
                "      \"JsonSchema\"\n" +
                "    ],\n" +
                "    \"submittableClassName\": \"uk.ac.ebi.subs.repository.model.Study\",\n" +
                "    \"archive\": \"Ena\",\n" +
                "    \"validationSchema\": {\n" +
                "      \"#dollar#schema\": \"http://json-schema.org/draft-07/schema#\",\n" +
                "      \"#dollar#async\": true,\n" +
                "      \"title\": \"Submissions Study Schema\",\n" +
                "      \"description\": \"A base validation study schema\",\n" +
                "      \"version\": \"1.0.0\",\n" +
                "      \"author\": \"subs\",\n" +
                "      \"type\": \"object\",\n" +
                "      \"properties\": {\n" +
                "        \"alias\": {\n" +
                "          \"description\": \"An unique identifier in a submission.\",\n" +
                "          \"type\": \"string\",\n" +
                "          \"minLength\": 1.0\n" +
                "        },\n" +
                "        \"title\": {\n" +
                "          \"description\": \"Title of the study.\",\n" +
                "          \"type\": \"string\"\n" +
                "        },\n" +
                "        \"description\": {\n" +
                "          \"description\": \"More extensive free-form description.\",\n" +
                "          \"type\": \"string\"\n" +
                "        },\n" +
                "        \"projectRef\": {\n" +
                "          \"description\": \"Reference to project.\",\n" +
                "          \"#dollar#ref\": \"#/definitions/submittableRef\"\n" +
                "        },\n" +
                "        \"protocolRefs\": {\n" +
                "          \"description\": \"Reference(s) to protocol(s).\",\n" +
                "          \"type\": \"array\",\n" +
                "          \"items\": {\n" +
                "            \"#dollar#ref\": \"#/definitions/submittableRef\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"attributes\": {\n" +
                "          \"description\": \"Attributes for describing a study.\",\n" +
                "          \"type\": \"object\",\n" +
                "          \"properties\": {\n" +
                "            \"study_abstract\": {\n" +
                "              \"minItems\": 1.0,\n" +
                "              \"maxItems\": 1.0,\n" +
                "              \"type\": \"array\",\n" +
                "              \"description\": \"Briefly describes the goals, purpose, and scope of the Study. This need not be listed if it can be inherited from a referenced publication.\",\n" +
                "              \"items\": {\n" +
                "                \"type\": \"object\",\n" +
                "                \"properties\": {\n" +
                "                  \"value\": {\n" +
                "                    \"type\": \"string\"\n" +
                "                  }\n" +
                "                }\n" +
                "              }\n" +
                "            },\n" +
                "            \"study_type\": {\n" +
                "              \"minItems\": 1.0,\n" +
                "              \"maxItems\": 1.0,\n" +
                "              \"type\": \"array\",\n" +
                "              \"description\": \"Define the type of the study\",\n" +
                "              \"items\": {\n" +
                "                \"type\": \"object\",\n" +
                "                \"properties\": {\n" +
                "                  \"value\": {\n" +
                "                    \"enum\": [\n" +
                "                      \"Whole Genome Sequencing\",\n" +
                "                      \"Metagenomics\",\n" +
                "                      \"Transcriptome Analysis\",\n" +
                "                      \"Resequencing\",\n" +
                "                      \"Epigenetics\",\n" +
                "                      \"Synthetic Genomics\",\n" +
                "                      \"Forensic or Paleo-genomics\",\n" +
                "                      \"Gene Regulation Study\",\n" +
                "                      \"Cancer Genomics\",\n" +
                "                      \"Population Genomics\",\n" +
                "                      \"RNASeq\",\n" +
                "                      \"Exome Sequencing\",\n" +
                "                      \"Pooled Clone Sequencing\",\n" +
                "                      \"Other\"\n" +
                "                    ]\n" +
                "                  }\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          \"patternProperties\": {\n" +
                "            \"^#dot#*$\": {\n" +
                "              \"type\": \"array\",\n" +
                "              \"minItems\": 1.0,\n" +
                "              \"items\": {\n" +
                "                \"type\": \"object\",\n" +
                "                \"properties\": {\n" +
                "                  \"value\": {\n" +
                "                    \"type\": \"string\",\n" +
                "                    \"minLength\": 1.0\n" +
                "                  },\n" +
                "                  \"units\": {\n" +
                "                    \"type\": \"string\",\n" +
                "                    \"minLength\": 1.0\n" +
                "                  },\n" +
                "                  \"terms\": {\n" +
                "                    \"type\": \"array\",\n" +
                "                    \"items\": {\n" +
                "                      \"type\": \"object\",\n" +
                "                      \"properties\": {\n" +
                "                        \"url\": {\n" +
                "                          \"type\": \"string\",\n" +
                "                          \"format\": \"uri\",\n" +
                "                          \"isValidTerm\": true\n" +
                "                        }\n" +
                "                      },\n" +
                "                      \"required\": [\n" +
                "                        \"url\"\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  }\n" +
                "                },\n" +
                "                \"required\": [\n" +
                "                  \"value\"\n" +
                "                ]\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      },\n" +
                "      \"definitions\": {\n" +
                "        \"submittableRef\": {\n" +
                "          \"type\": \"object\",\n" +
                "          \"properties\": {\n" +
                "            \"alias\": {\n" +
                "              \"type\": \"string\",\n" +
                "              \"minLength\": 1.0\n" +
                "            },\n" +
                "            \"accession\": {\n" +
                "              \"type\": \"string\",\n" +
                "              \"minLength\": 1.0\n" +
                "            },\n" +
                "            \"team\": {\n" +
                "              \"type\": \"string\",\n" +
                "              \"minLength\": 1.0\n" +
                "            }\n" +
                "          },\n" +
                "          \"anyOf\": [\n" +
                "            {\n" +
                "              \"required\": [\n" +
                "                \"alias\",\n" +
                "                \"team\"\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"required\": [\n" +
                "                \"accession\"\n" +
                "              ]\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      },\n" +
                "      \"required\": [\n" +
                "        \"alias\",\n" +
                "        \"title\"\n" +
                "      ]\n" +
                "    }\n" +
                "  },\n" +
                "  \"references\": {\n" +
                "    \"ProjectRef\": [\n" +
                "      {\n" +
                "        \"alias\": \"alias-193324\",\n" +
                "        \"team\": \"subs.api-tester-team-1\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
    }

}
