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

package mc.embedding.transitive;

import de.monticore.io.paths.ModelPath;
import de.monticore.symboltable.GlobalScope;
import de.monticore.symboltable.ResolvingConfiguration;
import mc.GeneratorIntegrationsTest;
import mc.embedding.transitive.transhost._symboltable.TransHostLanguage;
import mc.embedding.transitive.transhost._symboltable.TransStartSymbol;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TransHostTest extends GeneratorIntegrationsTest {

  @Test
  public void test() {
    final TransHostLanguage language = new TransHostLanguage();
    final ResolvingConfiguration resolvingConfiguration = new ResolvingConfiguration();
    resolvingConfiguration.addTopScopeResolvers(language.getResolvingFilters());

    final ModelPath modelPath = new ModelPath(Paths.get("src/test/resources/mc/embedding/transitive"));

    final GlobalScope scope = new GlobalScope(modelPath, language, resolvingConfiguration);

    TransStartSymbol hostSymbol = scope.<TransStartSymbol>resolve("TH", TransStartSymbol.KIND).orElse(null);
    assertNotNull(hostSymbol);
    assertEquals("TH", hostSymbol.getName());
  }

}
