package uk.ac.ebi.subs.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import uk.ac.ebi.subs.data.component.AbstractSubsRef;
import uk.ac.ebi.subs.repository.model.Analysis;
import uk.ac.ebi.subs.repository.model.Assay;
import uk.ac.ebi.subs.repository.model.AssayData;
import uk.ac.ebi.subs.repository.model.EgaDacPolicy;
import uk.ac.ebi.subs.repository.model.EgaDataset;
import uk.ac.ebi.subs.repository.model.Sample;
import uk.ac.ebi.subs.repository.model.SampleGroup;
import uk.ac.ebi.subs.repository.model.Study;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(JUnit4.class)
public class SubmittableRefsTests {

    @Test
    public void analysis_null_refs(){
        Analysis a = new Analysis();

        a.setAssayRefs(null);
        a.setStudyRefs(null);
        a.setSampleRefs(null);
        a.setAssayDataRefs(null);
        a.setAnalysisRefs(null);

        List<AbstractSubsRef> refs = a.refs().collect(Collectors.toList());
        Assert.assertTrue(refs.isEmpty());
    }

    @Test
    public void assay_null_refs(){
        Assay a = new Assay();

        a.setStudyRef(null);

        List<AbstractSubsRef> refs = a.refs().collect(Collectors.toList());
        Assert.assertTrue(refs.isEmpty());
    }

    @Test
    public void assay_data_null_refs(){
        AssayData ad = new AssayData();
        ad.setAssayRefs(null);

        List<AbstractSubsRef> refs = ad.refs().collect(Collectors.toList());
        Assert.assertTrue(refs.isEmpty());
    }

    @Test
    public void ega_dac_policy_null_refs(){
        EgaDacPolicy edp = new EgaDacPolicy();
        edp.setDacRef(null);

        List<AbstractSubsRef> refs = edp.refs().collect(Collectors.toList());
        Assert.assertTrue(refs.isEmpty());
    }

    @Test
    public void ega_dataset_null_refs(){
        EgaDataset ed = new EgaDataset();
        ed.setAnalysisRefs(null);
        ed.setDataRefs(null);
        ed.setEgaDacPolicyRef(null);

        List<AbstractSubsRef> refs = ed.refs().collect(Collectors.toList());
        Assert.assertTrue(refs.isEmpty());
    }

    @Test
    public void sample_null_refs(){
        Sample s = new Sample();
        s.setSampleRelationships(null);

        List<AbstractSubsRef> refs = s.refs().collect(Collectors.toList());
        Assert.assertTrue(refs.isEmpty());
    }

    @Test
    public void sample_group_null_refs(){
        SampleGroup sg = new SampleGroup();
        sg.setSampleRelationships(null);

        List<AbstractSubsRef> refs = sg.refs().collect(Collectors.toList());
        Assert.assertTrue(refs.isEmpty());
    }

    @Test
    public void study_null_refs(){
        Study s = new Study();
        s.setProjectRef(null);

        List<AbstractSubsRef> refs = s.refs().collect(Collectors.toList());
        Assert.assertTrue(refs.isEmpty());
    }



}
