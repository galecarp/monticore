/*
 * ******************************************************************************
 * MontiCore Language Workbench, www.monticore.de
 * Copyright (c) 2017, MontiCore, All rights reserved.
 *
 * This project is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this project. If not, see <http://www.gnu.org/licenses/>.
 * ******************************************************************************
 */

package de.monticore.codegen.mc2cd.transl;

import static de.monticore.codegen.mc2cd.TransformationHelper.typeToString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

import de.monticore.codegen.mc2cd.TestHelper;
import de.monticore.umlcd4a.cd4analysis._ast.ASTCDAttribute;
import de.monticore.umlcd4a.cd4analysis._ast.ASTCDClass;
import de.monticore.umlcd4a.cd4analysis._ast.ASTCDCompilationUnit;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.Slf4jLog;

/**
 * Test for the proper transformation of NonTerminals to corresponding ASTCDAttributes
 * 
 * @author Sebastian Oberhoff
 */
public class NonTerminalMultiplicityTest {

  private ASTCDClass astA;

  private ASTCDClass astB;

  private ASTCDClass astC;

  private ASTCDClass astD;

  private ASTCDClass astE;

  private ASTCDClass astF;

  private ASTCDClass astG;

  private ASTCDClass astH;

  private ASTCDClass astJ;

  public NonTerminalMultiplicityTest() {
    Slf4jLog.init();
    Log.enableFailQuick(false);
    ASTCDCompilationUnit cdCompilationUnit = TestHelper.parseAndTransform(Paths
        .get("src/test/resources/mc2cdtransformation/NonTerminalMultiplicityGrammar.mc4")).get();
    astA = TestHelper.getCDClass(cdCompilationUnit, "ASTA").get();
    astB = TestHelper.getCDClass(cdCompilationUnit, "ASTB").get();
    astC = TestHelper.getCDClass(cdCompilationUnit, "ASTC").get();
    astD = TestHelper.getCDClass(cdCompilationUnit, "ASTD").get();
    astE = TestHelper.getCDClass(cdCompilationUnit, "ASTE").get();
    astF = TestHelper.getCDClass(cdCompilationUnit, "ASTF").get();
    astG = TestHelper.getCDClass(cdCompilationUnit, "ASTG").get();
    astH = TestHelper.getCDClass(cdCompilationUnit, "ASTH").get();
    astJ = TestHelper.getCDClass(cdCompilationUnit, "ASTJ").get();
  }
  
  /**
   * Tests that the production "A = X;" generates a reference with the variable name X in the class
   * ASTA.
   */
  @Test
  public void testNonTerminalName() {
    List<ASTCDAttribute> attributes = astA.getCDAttributes();
    assertEquals("xs", attributes.get(0).getName());
  }
  
  /**
   * Tests that the production "A = X*;" results in the class ASTA having a field reference to
   * ASTXList.
   */
  @Test
  public void testStarMultiplicity() {
    List<ASTCDAttribute> attributes = astA.getCDAttributes();
    assertTrue(TestHelper.isListOfType(attributes.get(0).getType(),
        "mc2cdtransformation.NonTerminalMultiplicityGrammar.ASTX"));
  }
  
  /**
   * Tests that the production "B = (X)*;" results in the class ASTB having a field reference to
   * ASTXList.
   */
  @Test
  public void testParenthesizedStarMultiplicity() {
    List<ASTCDAttribute> attributes = astB.getCDAttributes();
    assertTrue(TestHelper.isListOfType(attributes.get(0).getType(),
        "mc2cdtransformation.NonTerminalMultiplicityGrammar.ASTX"));
  }
  
  /**
   * Tests that the production "C = X+;" results in the class ASTC having a field reference to
   * ASTXList.
   */
  @Test
  public void testPlusMultiplicity() {
    List<ASTCDAttribute> attributes = astC.getCDAttributes();
    assertTrue(TestHelper.isListOfType(attributes.get(0).getType(),
        "mc2cdtransformation.NonTerminalMultiplicityGrammar.ASTX"));
  }
  
  /**
   * Tests that the production "D = (X)+;" results in the class ASTD having a field reference to
   * ASTXList.
   */
  @Test
  public void testParenthesizedPlusMultiplicity() {
    List<ASTCDAttribute> attributes = astD.getCDAttributes();
    assertTrue(TestHelper.isListOfType(attributes.get(0).getType(),
        "mc2cdtransformation.NonTerminalMultiplicityGrammar.ASTX"));
  }
  
  /**
   * Tests that the production "E = X?;" results in the class ASTE having a field reference to
   * Optional<ASTX>.
   */
  @Test
  public void testOptionalMultiplicity() {
    List<ASTCDAttribute> attributes = astE.getCDAttributes();
    String name = typeToString(attributes.get(0).getType());
    assertEquals("Optional", name);
  }
  
  /**
   * Tests that the production "F = (X)?;" results in the class ASTF having a field reference to
   * Optional<ASTX>.
   */
  @Test
  public void testParenthesizedOptionalMultiplicity() {
    List<ASTCDAttribute> attributes = astF.getCDAttributes();
    String name = typeToString(attributes.get(0).getType());
    assertEquals("Optional", name);
  }
  
  /**
   * Tests that the production "G = (X) (X);" results in the class ASTG having a field reference to
   * ASTXList.
   */
  @Test
  public void testDuplicateMultiplicity() {
    List<ASTCDAttribute> attributes = astG.getCDAttributes();
    assertTrue(TestHelper.isListOfType(attributes.get(0).getType(),
        "mc2cdtransformation.NonTerminalMultiplicityGrammar.ASTX"));
  }
  
  /**
   * Tests that the production "H = X | Y;" results in the class ASTH having field references to
   * Optional<ASTX> and Optional<ASTY>.
   */
  @Test
  public void testAlternative() {
    List<ASTCDAttribute> attributes = astH.getCDAttributes();
    
    String xTypeName = typeToString(attributes.get(0).getType());
    assertEquals("Optional", xTypeName);
    
    String yTypeName = typeToString(attributes.get(1).getType());
    assertEquals("Optional", yTypeName);
  }
  
  /**
   * Tests that the production "J = X | X;" results in the class ASTJ having only a single reference to
   * Optional<ASTX> .
   */
  @Test
  public void testTwinAlternative() {
    List<ASTCDAttribute> attributes = astJ.getCDAttributes();
    assertEquals(1, attributes.size());
    String xTypeName = typeToString(attributes.get(0).getType());
    assertEquals("Optional", xTypeName);
  }
}
