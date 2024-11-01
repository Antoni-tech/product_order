import {Box, ChakraProps, Flex, Text} from "@chakra-ui/react";
import {FC} from "react";
import {useDispatch} from "react-redux";
import {useLocation, useNavigate} from "react-router";
import {PreventNavigationModal} from "../../shared/utilities/PreventNavigationModal";
import {BreadcrumbsData} from "../../shared/entities/Breadcrumbs/BreadCrumbsData";


/**
 * Компонент бредкрабмс - это стандартные хлебные крошки
 * на всех страницах портала
 *
 */
export const Breadcrumbs: FC = () => {
    const savedBreadItems = sessionStorage.getItem("breadcrumbsData");
    let breadcrumbsData: Array<BreadcrumbsData> = [];
    if (savedBreadItems) {
        breadcrumbsData = JSON.parse(savedBreadItems);
     }
    const location = useLocation()
    const dispath = useDispatch()
    const cannotNavigateWithoutModal = location.pathname.includes("edit") || location.pathname.includes("create") || location.pathname.includes("connect") || location.pathname.includes("copy")

    const navigate = useNavigate()
    const BreadcrumbWrapperStyleConfig: ChakraProps = {
        fontFamily: "Oswald",
        fontWeight: "300",
        fontSize: "24px",
    }
    const NavigationLogic = (canNavigate: boolean, navFunc: Function) => {
        !canNavigate ? navFunc() : PreventNavigationModal(dispath, navFunc)
    }

    function truncateText(text: string, maxLength: number) {
        if (text.length <= maxLength) {
            return text;
        } else {
            const truncatedText = '...' + text.substring(text.length - maxLength);
            return truncatedText;
        }
    }

    const Links = breadcrumbsData?.map((elem: BreadcrumbsData, idx: number) => {
        const isLast = breadcrumbsData.length - 1 === idx;
        const truncatedText = truncateText(elem.text, 50); // Обрезаем текст и начинаем с трех точек

        return (
            <Box display="flex" flexDir="row" key={idx}>
                {!isLast ? (
                    <Text
                        color="gray.400"
                        cursor="pointer"
                        children={truncatedText}
                        onClick={() =>
                            NavigationLogic(
                                cannotNavigateWithoutModal,
                                () => navigate(elem.link)
                            )
                        }
                    />
                ) : (
                    <Text
                        children={truncatedText}
                        color="gray.400"
                    />
                )}
                <Text
                    color={isLast ? "transparent" : "gray.400"}
                    children={isLast ? "" : "/"}
                />
            </Box>
        );
    });

    return (
        <>
            {Links?.length > 0 && <Flex {...BreadcrumbWrapperStyleConfig}>
                {Links}
            </Flex>}
        </>
    )
}
