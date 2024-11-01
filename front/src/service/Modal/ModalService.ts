import { PayloadAction } from "@reduxjs/toolkit";
import { Dispatch } from "react";
import { ModalTypes } from "../../redux/Modal/ModalReducer";
import { CustomModalConfig } from "../../shared/entities/Modal/ModalConfig";

export class ModalService {
    static setModalData(arg0: { onsubmit: (nextState?: Partial<import("formik").FormikState<import("../../shared/entities/Role/Role").Role>> | undefined) => void; }) {
        throw new Error("Method not implemented.");
    }
    private dispatch: Dispatch<PayloadAction<any>>
    constructor(dispatch: Dispatch<PayloadAction<any>>) {
        this.dispatch = dispatch
    }

    public setModalData(data: CustomModalConfig) {
        this.dispatch({
            type: ModalTypes.MODAL_ADD,
            payload: data
        })
    }

    public deleteModalData() {
        this.dispatch({
            type: ModalTypes.MODAL_DELETE,
            payload: null
        })
    }
}
