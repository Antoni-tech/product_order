import React from "react";
import {Box, ChakraProps, Link, Modal, ModalBody, ModalContent, Text,} from "@chakra-ui/react";
import {useDispatch} from "react-redux";
import {URLPaths} from "../../config/application/URLPaths";
import {useNavigate} from "react-router";
import {useAuth} from "../../hooks/AuthHook";
import {AuthorizationController} from "../../controllers/AuthController";
import {usePrivileges} from "../../hooks/PrivilegesProvider";

interface MenuModalProps {
    isOpen: boolean;
    onClose: () => void;
    buttonPosition: { top: number; left: number };
}

const MenuModal: React.FC<MenuModalProps> = ({isOpen, onClose, buttonPosition}) => {
    const dispatch = useDispatch();
    const authController = new AuthorizationController(dispatch);
    const navigate = useNavigate();
    const {handleAuthChange} = useAuth();
    const {priv} = usePrivileges()

    const LogOut = () => {
        authController.logOut().then((res) => {
            sessionStorage.clear();
            handleAuthChange(false);
            window.location.reload();
        });
        onClose();
        navigate(URLPaths.HOME.link);
    };

    const handleLinkClick = (path: string) => {
        onClose();
        navigate(path);
    };

    const lineStyle: ChakraProps = {
        borderBottom: "1px solid #B3B2B2",
        mt: "5px",
        mb: "5px",
    };
    const minLeftDistance = 93;
    return (
        <Modal isOpen={isOpen} onClose={onClose} isCentered motionPreset="none" closeOnOverlayClick={true}>
            <ModalContent style={{
                minWidth: "125px",
                position: "absolute",
                top: buttonPosition.top + 50,
                left: `calc(${Math.min(buttonPosition.left, 100 - minLeftDistance)}%)`,
                boxShadow: "0 0 0 1px #B3B2B2"
            }}>
                <ModalBody>
                    <Box fontWeight="400" fontFamily={"Oswald"} fontSize={"15px"} textAlign= "center" >
                        {(priv?.USER_CREATE || priv?.USER_VIEW) && (
                            <>
                                <Box {...lineStyle} w="100%"></Box>
                                <Link onClick={() => handleLinkClick(URLPaths.ACCOUNTS.link)}>
                                    <Text _hover={{textDecoration: "underline"}}>Users list</Text>
                                </Link>
                            </>
                        )}
                        {(priv?.ROLE_CREATE || priv?.ROLE_VIEW) && (
                            <>
                                <Box {...lineStyle} w="100%"></Box>
                                <Link onClick={() => handleLinkClick(URLPaths.ACCOUNTS_SECTION.link)}>
                                    <Text _hover={{textDecoration: "underline"}}>Role list</Text>
                                </Link>
                                <Box {...lineStyle} w="100%"></Box>
                            </>
                        )}
                        <Link onClick={() => LogOut()}>
                            <Text _hover={{textDecoration: "underline"}}>Log Out</Text>
                        </Link>
                    </Box>
                </ModalBody>
            </ModalContent>
        </Modal>
    );
};

export default MenuModal;
