import {Form, Formik} from "formik";
import React, {FC} from "react";
import {useDispatch} from "react-redux";
import {Box, Heading} from "@chakra-ui/react";
import {useParams} from "react-router";
import {Card} from "../Card/Card";
import {BoxStyleConfig} from "../../views/Forms/FormStyleConfigs";

export const ManagerModelspage: FC<{ edit: boolean }> = (edit) => {
    const dispatch = useDispatch();

    const ConnectorFormCardConfig = {position: "relative"}
    const {id} = useParams();
    return (
        <Card {...ConnectorFormCardConfig} >
            <Heading as='h4' size='md' ml={180} mb={"30px"}>
                {edit ? 'МОДУЛИ' :
                    ''}
            </Heading>
                    <Form autoComplete="off">
                        <Box {...BoxStyleConfig}>
                            <label htmlFor="Наименование">Наименование:</label>
                            <input />
                        </Box>
                    </Form>
        </Card>
    );
}
