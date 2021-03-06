<#--
****************************************************************************
MontiCore Language Workbench, www.monticore.de
Copyright (c) 2017, MontiCore, All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

3. Neither the name of the copyright holder nor the names of its
contributors may be used to endorse or promote products derived from this
software without specific prior written permission.

This software is provided by the copyright holders and contributors
"as is" and any express or implied warranties, including, but not limited
to, the implied warranties of merchantability and fitness for a particular
purpose are disclaimed. In no event shall the copyright holder or
contributors be liable for any direct, indirect, incidental, special,
exemplary, or consequential damages (including, but not limited to,
procurement of substitute goods or services; loss of use, data, or
profits; or business interruption) however caused and on any theory of
liability, whether in contract, strict liability, or tort (including
negligence or otherwise) arising in any way out of the use of this
software, even if advised of the possibility of such damage.
****************************************************************************
-->
<#--
  Generates a Java interface
  
  @params    ASTCDEnum $ast
  @result    mc.javadsl.JavaDSL.CompilationUnit
  
-->
<#assign genHelper = glex.getGlobalVar("astHelper")>
${tc.defineHookPoint("EnumContent:addComment")}
<#-- set package -->
package ${genHelper.getAstPackage()};
<#-- Imports hook --> 
${tc.defineHookPoint("<Block>?EnumContent:addImports")}

<#assign interfaces = ast.printInterfaces()>

public enum ${ast.getName()}<#if interfaces?has_content> implements ${interfaces}</#if> {
  <#assign count = 0>
  <#list ast.getCDEnumConstants() as constant>
    <#if count == 0>
      ${constant.getName()}(ASTConstants${genHelper.getCdName()}.${constant.getName()}) 
    <#else>
      ,${constant.getName()}(ASTConstants${genHelper.getCdName()}.${constant.getName()})
    </#if>
    <#assign count = count + 1>
  </#list>
  ;
 
  protected int intValue;
  
  private ${ast.getName()}(int intValue){
    this.intValue=intValue;
  }
  
  public int intValue() {
    return intValue;
  }
  
<#list ast.getCDMethods() as method>
  ${tc.includeArgs("ast.ClassMethod", [method, ast])}
</#list>
}
