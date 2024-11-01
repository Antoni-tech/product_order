import { ChakraProps, Modal, ModalBody, ModalCloseButton, ModalContent, ModalFooter, ModalOverlay } from "@chakra-ui/react";
import { FC } from "react";
import { CustomModalConfig } from "../../shared/entities/Modal/ModalConfig";
import { FormButtonsRow } from "../Buttons/FormButtonsRow";

/**
 * CustomModal.
 *
 * @param {CustomModalConfig}
 *
 * This component represents modals, that will appear 
 * in response to arbitrary events
 */
export const CustomModal: FC<CustomModalConfig> = ({ style, isOpen, onOpen, onSubmit, onClose, idToDelete, modalTextData, context }) => {

    const CustomModalStylesConfig: ChakraProps = {
        p: "24px 16px",
        fontFamily: "Raleway-Regular",
        fontSize: "16px",
        textAlign: "center",
        maxHeight: "450px",
        width: "auto",
        minW: "900px",
        overflowX: "hidden",
        overflowY: "auto",
        whiteSpace: "pre",
        ...style
    }

    return (
        <Modal closeOnOverlayClick={false} isOpen={isOpen} onClose={onClose} isCentered={true} >
            <ModalOverlay />
            <ModalContent {...CustomModalStylesConfig}>
                <ModalCloseButton />
                <ModalBody py="32px">
                        {modalTextData}
                </ModalBody>
                {onSubmit && <ModalFooter pos="relative">
                    <FormButtonsRow
                        config={{
                            submitButton: onSubmit === undefined ? undefined : {
                                text: context === "action" ? "Yes" : context === "error" ? "Ok" : "Remove",
                                disabled: false,
                                clickAction: () => onSubmit()
                            },
                            cancelButton: onClose !== undefined && context !== "error"
                                ? {
                                    text: context === "action" ? "No" : "Chancel",
                                    cancelFunc: onClose
                                }
                                : undefined
                        }}
                    />
                </ModalFooter>}
            </ModalContent>
        </Modal>
    )
}
