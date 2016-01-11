package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.Schema;
import de.brands4friends.aig.parser.FileProcessor;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.expect;

public class BuildPipelineTest extends EasyMockSupport{

    private BuildPipeline instance;
    private FileProcessor mockFileProcessor;
    private IllustrationGenerator mockIllustrationGenerator;

    @Before
    public void setUp() throws Exception {
        mockFileProcessor = createMock(FileProcessor.class);
        mockIllustrationGenerator = createMock(IllustrationGenerator.class);
        instance = new BuildPipeline(mockFileProcessor,mockIllustrationGenerator);
    }

    @Test
    public void testExecute() throws Exception {
        final String inputFileName = "my-input-filename";
        final String outputFileName = "my-output-filename";
        final int outerboundX = 600;
        Schema mockSchema = createMock(Schema.class);
        expect(mockFileProcessor.readFromFile(inputFileName)).andReturn(mockSchema);
        mockIllustrationGenerator.createIllustration(mockSchema,outputFileName,outerboundX);
        replayAll();
        instance.execute(inputFileName,outputFileName,outerboundX);
        verifyAll();
    }
}