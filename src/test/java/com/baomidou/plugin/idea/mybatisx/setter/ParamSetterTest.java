package com.baomidou.plugin.idea.mybatisx.setter;

import com.baomidou.plugin.idea.mybatisx.util.SqlUtils;
import com.intellij.testFramework.UsefulTestCase;

import java.io.IOException;

public class ParamSetterTest extends UsefulTestCase {

    public void testParseLog() throws IOException {
        String log = "==> Preparing: select * from table where id = ?\n";
        log += "==> Parameters: 123(String)";
        String result = SqlUtils.parseExecutableSql(log);

        System.out.println(result);
    }

    public void testParseLog1() {
        String s = "==>  Preparing: DELETE FROM exempt_course_student WHERE (school_id = ? AND course_no IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?))\n" +
                   "==> Parameters: S-001(String), 1765279912239878146(String), 1765279912365707265(String), 1765279912608976898(String), 1765279912764166146(String), 1765279913070350337(String), 1765279913187790850(String), 1765279913556889602(String), 1765279913657552898(String), 1765279913774993411(String), 1765280943707312129(String), 1765281101278924802(String), 1765279913330397186(String), 1765279913435254786(String), 1772426391459012609(String), 1772426391576453122(String), af2020a2-827a-4935-9a4b-2d3bad650836(String), 005fc64f-2943-4ab9-816d-042e691eb1c6(String), 1772426391089913857(String), 1772426392042020866(String), 1772426392163655681(String), 1772426391328989185(String), 1772426391702282241(String), 1772426391865860098(String), 6e302db1-ca03-4ef7-87b5-4ad2d4e86ea8(String), b53e09eb-3171-4dec-8357-2b28863a14ae(String), 4996df6e-fff4-4362-8d1c-e1d6743eb3ee(String), 1772426392289484801(String), 1773245613437181953(String), 1773245616087982081(String), 1773245617757315074(String), 1773245618982051841(String), 1773245620466835458(String), 1773245621603491841(String), 1777134310607880193(String), 1777134310880509954(String), 1777134311027310594(String), 1777134311220248577(String), 1777134311392215042(String), 1777134311601930242(String), 1777134311761313793(String), 1777134311916503041(String), 1777134312046526465(String), 1777256419719970818(String), 1778706379552399362(String), 1778708364095725570(String), 1779793611801567233(String)\n" +
                   "<==    Updates: 0";

        String sql = SqlUtils.parseExecutableSql(s);
        System.out.println(sql);
    }
}
