package com.baomidou.plugin.idea.mybatisx.jpa.file;

import com.baomidou.plugin.idea.mybatisx.util.ClassCreator;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.testFramework.IdeaTestUtil;
import com.intellij.testFramework.builders.JavaModuleFixtureBuilder;
import com.intellij.testFramework.fixtures.JavaCodeInsightFixtureTestCase;

import java.util.HashSet;
import java.util.Set;

public class CreateFileTest extends JavaCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/module";
    }


    public void testCreateFile() {
        myFixture.configureByFiles("domain/EntityClass.java", "domain/EntityParentClass.java");

        Project project = getProject();
        Set<String> allowFields = new HashSet<>();
        allowFields.add("name");
        allowFields.add("amount");

        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        GlobalSearchScope globalSearchScope = GlobalSearchScope.allScope(project);
        PsiClass entityClass = javaPsiFacade.findClass("domain.EntityClass", globalSearchScope);
        String entityClassIdAgeDTO = "EntityClassIdAgeDTO";
        ClassCreator classCreator = new ClassCreator();
        classCreator.createFromAllowedFields(allowFields, entityClass, entityClassIdAgeDTO);

    }

    @Override
    protected void tuneFixture(JavaModuleFixtureBuilder moduleBuilder) throws Exception {
        super.tuneFixture(moduleBuilder);

        String javaHome = System.getenv("JAVA_HOME");
        if (javaHome == null) {
            javaHome = IdeaTestUtil.getMockJdk18Path().getPath();
        }

        moduleBuilder.addJdk(javaHome);

        moduleBuilder.addLibrary("mp3-lib", "src/test/testData/lib");

    }
}
