import React, {useEffect, useRef, useState} from "react";
import {Grid as GridDHX, DataCollection} from "dhx-suite";
import "dhx-suite/codebase/suite.min.css";
import {Box} from "@chakra-ui/react";

interface GridProps {
    columns?: Array<any>;
    spans?: Array<any>;
    data?: Array<any> | DataCollection;
    headerRowHeight?: number;
    footerRowHeight?: number;
    rowHeight?: number;
    width?: number;
    height?: number;
    sortable?: boolean;
    rowCss?: Function;
    splitAt?: number;
    selection?: "cell" | "row" | "complex" | undefined;
    autoWidth?: boolean;
    autoHeight?: boolean;
    css?: string;
    resizeable?: boolean;
    multiselection?: boolean;
    keyNavigation?: boolean;
    htmlEnable?: boolean;
    editable?: boolean;
    dragMode?: "target" | "source" | "both";
    dragCopy?: boolean;
    adjust?: boolean;
    autoEmptyRow?: boolean;
    onReady: (item: GridDHX) => void;
}

const Grid: React.FC<GridProps> = (props) => {
    const [grid, setGrid] = useState<GridDHX>();
    const elRef = useRef<HTMLDivElement | null>(null);

    useEffect(() => {
        if (elRef.current) {
            const newGrid = new GridDHX(elRef.current, {
                columns: props.columns,
                headerRowHeight: props.headerRowHeight,
                width: props.width,
                height: props.height,
                rowHeight: props.rowHeight,
                adjust: props.adjust,
                autoWidth: props.autoWidth,
                autoHeight: props.autoHeight,
                editable: props.editable,
                selection: props.selection,
                multiselection: props.multiselection,
            });
            if (grid) {
                grid.destructor();
            }
            setGrid(newGrid);
            if (props.onReady) {
                props.onReady(newGrid);
            }
        }
    }, [props.width, props.rowHeight, props.height]);

    return (
        <Box ref={elRef}></Box>
    );
};

export default Grid;
