import React, {useEffect, useRef, useState} from "react";
import {Form as FormDHX} from "dhx-suite";

import {FormControl} from "@chakra-ui/react";
import {useFormikContext} from "formik";
import {useParams} from "react-router";
import {useDispatch} from "react-redux";
import {useAppSelector} from "../../../../redux/Store";
import formConfig from "./Models.json";
import {ModelsController} from "../../../../controllers/ModelsController";

interface FormProps {
    css?: string;
    width?: string | number;
    height?: string | number;
    rows?: any[];
    cols?: any[];
    title?: string;
    align?: "start" | "center" | "end" | "between" | "around" | "evenly";
    padding?: string | number;
    disabled?: boolean;
}

const DHXFormModels: React.FC<FormProps> = (props) => {
    const [form, setForm] = useState<FormDHX>();
    const formInConnectorRef = useRef<HTMLDivElement | null>(null);
    const formik = useFormikContext();
    const {id} = useParams();
    const dispatch = useDispatch();
    const modelsController = new ModelsController(dispatch);
    const model = useAppSelector(store => store.ModelReducer.model)


    useEffect(() => {
        if (form && model) {
            Object.entries(model).forEach(([key, value]) => {
                    form?.setValue({[key]: value});
            });
        }
    }, [model]);

    useEffect(() => {
        if (formInConnectorRef.current && !form) {
            setForm(new FormDHX(formInConnectorRef.current, formConfig));
        }
        if (id !== null && id !== undefined && form) {
            modelsController.getModel(id).then(() => {
            })
        }
        if (form) {
            form?.forEach((item, index, array) => {
                if (item.config.name) {
                    formik.setFieldValue(item.config.name, form.getItem(item.config.name).getValue(), false);
                }
            });
            form?.events.on("click", (id) => console.log(id, "click"));
            form?.events.on("change", (id: any, events: any) => {
                    formik.setFieldValue(id, events, false);
                }
            );
        }
    }, [id, form]);

    return <FormControl style={{textAlign: "left", background: "#fff", width: "80%"}} ref={formInConnectorRef}/>
};

export default DHXFormModels;
