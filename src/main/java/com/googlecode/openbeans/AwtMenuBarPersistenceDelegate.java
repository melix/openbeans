/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.googlecode.openbeans;

import java.awt.Menu;
import java.awt.MenuBar;
import com.googlecode.openbeans.DefaultPersistenceDelegate;
import com.googlecode.openbeans.Encoder;
import com.googlecode.openbeans.Expression;
import com.googlecode.openbeans.PersistenceDelegate;
import com.googlecode.openbeans.Statement;

class AwtMenuBarPersistenceDelegate extends DefaultPersistenceDelegate {
    @Override
	@SuppressWarnings({ "nls", "boxing" })
    protected void initialize(Class<?> type, Object oldInstance,
			Object newInstance, Encoder enc) {
		super.initialize(type, oldInstance, newInstance, enc);
		if (type != oldInstance.getClass()) {
            return;
        }
		
		MenuBar bar = (MenuBar) oldInstance;
		int count = bar.getMenuCount();
		Expression getterExp = null;
		for (int i = 0; i < count; i++) {
			getterExp = new Expression(bar.getMenu(i), "getLabel", null);
			try {
				// Calculate the old value of the property
				Object oldVal = getterExp.getValue();
				// Write the getter expression to the encoder
				enc.writeExpression(getterExp);
				// Get the target value that exists in the new environment
				Object targetVal = enc.get(oldVal);
				// Get the current property value in the new environment
				Object newVal = null;
				try {
					newVal = new Expression(((MenuBar) newInstance).getMenu(i),
							"getLabel", null).getValue();
				} catch (IndexOutOfBoundsException ex) {
					// The newInstance has no elements, so current property
					// value remains null
				}
				/*
				 * Make the target value and current property value equivalent
				 * in the new environment
				 */
				if (null == targetVal) {
					if (null != newVal) {
						// Set to null
						Statement setterStm = new Statement(oldInstance, "insert",
								new Object[] { null, i });
						enc.writeStatement(setterStm);
					}
				} else {
					PersistenceDelegate pd = enc
							.getPersistenceDelegate(targetVal.getClass());
					if (!pd.mutatesTo(targetVal, newVal)) {
						Menu menu = new Menu((String) oldVal);
						menu.setName(bar.getMenu(i).getName());
						Statement setterStm = new Statement(oldInstance,
								"add", new Object[] { menu });
						enc.writeStatement(setterStm);
					}
				}
			} catch (Exception ex) {
				enc.getExceptionListener().exceptionThrown(ex);
			}
		}
	}
}
