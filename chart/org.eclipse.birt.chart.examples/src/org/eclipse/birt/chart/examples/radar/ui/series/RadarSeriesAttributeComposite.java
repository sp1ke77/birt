/***********************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.radar.ui.series;

import org.eclipse.birt.chart.examples.radar.i18n.Messages;
import org.eclipse.birt.chart.examples.radar.model.type.RadarSeries;
import org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.MarkerEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.TristateCheckbox;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 * 
 */
public class RadarSeriesAttributeComposite extends Composite implements
		Listener
{

	private MarkerEditorComposite mec = null;

	private RadarSeries series = null;

	private ChartWizardContext context;

	private LineAttributesComposite liacLine = null;

	private TristateCheckbox btnPalette;

	private TristateCheckbox btnConnectEndPoints;

	private TristateCheckbox btnFillPoly;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.examples/swt.series" ); //$NON-NLS-1$
	
	public static final String SUBTASK_YSERIES_RADAR = ChartHelpContextIds.PREFIX
			+ "FormatRadarChartSeries_ID"; //$NON-NLS-1$
	
	/**
	 * @param parent
	 * @param style
	 */
	public RadarSeriesAttributeComposite( Composite parent, int style,
			ChartWizardContext context, Series series )
	{
		super( parent, style );

		if ( !( series instanceof RadarSeriesImpl ) )
		{
			try
			{
				throw new ChartException( ChartUIExtensionPlugin.ID,
						ChartException.VALIDATION,
						"RadarSeriesAttributeComposite.Exception.IllegalArgument", new Object[]{series.getClass( ).getName( )}, Messages.getResourceBundle( ) ); //$NON-NLS-1$
			}
			catch ( ChartException e )
			{
				logger.log( e );
				e.printStackTrace( );
			}
		}
		this.series = (RadarSeries) series;
		this.context = context;

		init( );
		placeComponents( );

		ChartUIUtil.bindHelp( parent, SUBTASK_YSERIES_RADAR );
	}

	private void init( )
	{
		this.setSize( getParent( ).getClientArea( ).width,
				getParent( ).getClientArea( ).height );
	}

	private void placeComponents( )
	{
		// Main content composite
		this.setLayout( new GridLayout( ) );

		// individual series
		Group grpLine2 = new Group( this, SWT.NONE );
		GridLayout glLine2 = new GridLayout( 2, false );
		glLine2.horizontalSpacing = 0;
		grpLine2.setLayout( glLine2 );
		grpLine2.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		grpLine2.setText( Messages.getString( "RadarSeriesMarkerSheet.Label.Series" ) ); //$NON-NLS-1$

		int lineStyles = LineAttributesComposite.ENABLE_AUTO_COLOR
				| LineAttributesComposite.ENABLE_COLOR
				| LineAttributesComposite.ENABLE_STYLES
				| LineAttributesComposite.ENABLE_VISIBILITY
				| LineAttributesComposite.ENABLE_WIDTH;
		liacLine = new LineAttributesComposite( grpLine2,
				SWT.NONE,
				lineStyles,
				context,
				series.getLineAttributes( ) );
		GridData gdLIACLine = new GridData( );
		gdLIACLine.verticalSpan = 4;
		gdLIACLine.widthHint = 200;
		liacLine.setLayoutData( gdLIACLine );
		liacLine.addListener( this );

		Composite cmp = new Composite( grpLine2, SWT.NONE );
		cmp.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		cmp.setLayout( new GridLayout( 1, false) );

		btnPalette = new TristateCheckbox( cmp, SWT.NONE );
		{
			btnPalette.setText( Messages.getString( "RadarSeriesAttributeComposite.Lbl.LinePalette" ) ); //$NON-NLS-1$
			btnPalette.setSelectionState( series.isSetPaletteLineColor( ) ? ( series.isPaletteLineColor( ) ? TristateCheckbox.STATE_SELECTED
					: TristateCheckbox.STATE_UNSELECTED )
					: TristateCheckbox.STATE_GRAYED );
			btnPalette.addListener( SWT.Selection, this );
		}
		
		btnConnectEndPoints = new TristateCheckbox( cmp, SWT.NONE );
		{
			btnConnectEndPoints.setText( Messages.getString( "RadarSeriesAttributeComposite.Lbl.ConnectPoints" ) ); //$NON-NLS-1$	
			btnConnectEndPoints.setSelectionState( series.isSetConnectEndpoints( ) ? ( series.isConnectEndpoints( ) ? TristateCheckbox.STATE_SELECTED
					: TristateCheckbox.STATE_UNSELECTED )
					: TristateCheckbox.STATE_GRAYED );
			btnConnectEndPoints.addListener( SWT.Selection, this );
		}
		
		btnFillPoly = new TristateCheckbox( cmp, SWT.NONE );
		{
			btnFillPoly.setText( Messages.getString( "RadarSeriesAttributeComposite.Lbl.FillPoly" ) ); //$NON-NLS-1$
			btnFillPoly.setSelectionState( series.isSetFillPolys( ) ? ( series.isFillPolys( ) ? TristateCheckbox.STATE_SELECTED
					: TristateCheckbox.STATE_UNSELECTED )
					: TristateCheckbox.STATE_GRAYED );
			btnFillPoly.addListener( SWT.Selection, this );
			btnFillPoly.setEnabled( btnConnectEndPoints.getSelectionState( ) == TristateCheckbox.STATE_SELECTED );
		}

		Group grpMarker = new Group( cmp, SWT.NONE );
		GridData gd = new GridData();
		gd.horizontalSpan=2;
		grpMarker.setLayoutData( gd );
		grpMarker.setText( Messages.getString( "RadarSeriesMarkerSheet.GroupLabel.Markers" ) ); //$NON-NLS-1$
		grpMarker.setLayout( new GridLayout( 2, false ) );

		// Layout for marker
		Label lblMarker = new Label( grpMarker, SWT.NONE );
		lblMarker.setText( Messages.getString( "RadarSeriesMarkerSheet.Label.Markers" ) ); //$NON-NLS-1$

		mec = new MarkerEditorComposite( grpMarker, series.getMarker( ) );

		enableLineSettings( series.getWebLineAttributes( ).isVisible( ) );
		enableLineSettings( series.getLineAttributes( ).isVisible( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.
	 * Event)
	 */
	public void handleEvent( Event event )
	{
		boolean isUnset = ( event.detail == ChartElementUtil.PROPERTY_UNSET );
		if ( event.widget.equals( liacLine ) )
		{
			if ( event.type == LineAttributesComposite.VISIBILITY_CHANGED_EVENT )
			{
				ChartElementUtil.setEObjectAttribute( series.getLineAttributes( ),
						"visible",//$NON-NLS-1$
						( (Boolean) event.data ).booleanValue( ),
						isUnset );
				enableLineSettings( series.getLineAttributes( ).isSetVisible( )
						&& series.getLineAttributes( ).isVisible( ) );
			}
			else if ( event.type == LineAttributesComposite.STYLE_CHANGED_EVENT )
			{
				ChartElementUtil.setEObjectAttribute( series.getLineAttributes( ),
						"style",//$NON-NLS-1$
						(LineStyle) event.data,
						isUnset );
			}
			else if ( event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT )
			{
				ChartElementUtil.setEObjectAttribute( series.getLineAttributes( ),
						"thickness",//$NON-NLS-1$
						( (Integer) event.data ).intValue( ),
						isUnset );
			}
			else if ( event.type == LineAttributesComposite.COLOR_CHANGED_EVENT )
			{
				series.getLineAttributes( )
						.setColor( (ColorDefinition) event.data );
			}
		}
		else if ( event.widget.equals( btnPalette ) )
		{
			ChartElementUtil.setEObjectAttribute( series,
					"paletteLineColor",//$NON-NLS-1$
					btnPalette.getSelectionState( ) == TristateCheckbox.STATE_SELECTED,
					btnPalette.getSelectionState( ) == TristateCheckbox.STATE_GRAYED );
		}
		else if ( event.widget.equals( btnFillPoly ) )
		{
			ChartElementUtil.setEObjectAttribute( series,
					"fillPolys",//$NON-NLS-1$
					btnFillPoly.getSelectionState( ) == TristateCheckbox.STATE_SELECTED,
					btnFillPoly.getSelectionState( ) == TristateCheckbox.STATE_GRAYED );
		}
		else if ( event.widget.equals( btnConnectEndPoints ) )
		{
			ChartElementUtil.setEObjectAttribute( series,
					"connectEndpoints",//$NON-NLS-1$
					btnConnectEndPoints.getSelectionState( ) == TristateCheckbox.STATE_SELECTED,
					btnConnectEndPoints.getSelectionState( ) == TristateCheckbox.STATE_GRAYED );
			btnFillPoly.setEnabled( btnConnectEndPoints.getSelectionState( ) == TristateCheckbox.STATE_SELECTED );
		}
		else if ( event.widget.equals( mec ) )
		{
			series.setMarker( mec.getMarker( ) );
		}
	}

	private void enableLineSettings( boolean isEnabled )
	{
		if ( btnPalette != null )
		{
			btnPalette.setEnabled( isEnabled );
			btnConnectEndPoints.setEnabled( isEnabled );
		}
	}
}