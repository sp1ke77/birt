/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.postscript.truetypefont;

import java.io.IOException;


public interface ITrueTypeWriter
{
	void close() throws IOException;
	
	void initialize() throws IOException;
	
	void useDisplayName(String displayName ) throws IOException;
	
	void ensureGlyphAvailable( char c ) throws IOException;
	
	void ensureGlyphsAvailable( String string ) throws IOException;

	String toHexString( String text );
}
