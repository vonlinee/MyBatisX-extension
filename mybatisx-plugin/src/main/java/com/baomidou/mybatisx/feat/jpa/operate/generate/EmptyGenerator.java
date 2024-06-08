package com.baomidou.mybatisx.feat.jpa.operate.generate;

import com.baomidou.mybatisx.dom.model.Mapper;
import com.baomidou.mybatisx.feat.jpa.component.TxField;
import com.baomidou.mybatisx.util.MapperUtils;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

public class EmptyGenerator implements Generator {
    private Logger logger = LoggerFactory.getLogger(EmptyGenerator.class);

    @Override
    public void generateSelect(String id, String value, Boolean resultType, String resultMap, String resultSet, List<TxField> resultFields, PsiClass entityClass) {
        logger.warn("generateSelect fail");
    }

    @Override
    public void generateDelete(String id, String value) {
        logger.warn("generateDelete fail");
    }

    @Override
    public void generateInsert(String id, String value) {
        logger.warn("generateInsert fail");
    }

    @Override
    public void generateUpdate(String id, String value) {
        logger.warn("generateUpdate fail");
    }

    @Override
    public boolean checkCanGenerate(PsiClass mapperClass) {
        final Collection<Mapper> mappers = MapperUtils.findMappers(mapperClass.getProject(), mapperClass);
        if (!hasMapperXmlFiles(mappers)) {
            final String message = "mapper :'" + mapperClass.getQualifiedName() + "'\n is not related mapper xml";
            Messages.showWarningDialog(message, "Generate Failure");
            return false;
        }
        return true;
    }

    private boolean hasMapperXmlFiles(Collection<Mapper> mappers) {
        return !mappers.isEmpty();
    }
}
