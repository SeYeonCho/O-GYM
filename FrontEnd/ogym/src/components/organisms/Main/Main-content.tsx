import { Carousel, Image } from "antd";
import styled from "styled-components";
import crossFit from '../../../assets/pages/mainPage/carousel/crossfit.jpg';
import dumbBell from '../../../assets/pages/mainPage/carousel/dumbbells.jpg';
import weightLift from '../../../assets/pages/mainPage/carousel/weight_lifting.jpg';
const StyledCarousel = styled(Carousel)`

 .slick-dots li button {
    // background: red;
    opacity: 0.4;
}

 .slick-dots li.slick-active button {
    opacity: 1;
    // background: #ff4ef6;
    // animation: fillBackground 1s infinite
    background: linear-gradient(to left, rgba(0,0,0,0.4) 50%, #fff 50%);
    background-size: 200% 100%;
    background-position: bottom right;
    transition: all 3s ease-in;
}

@keyframes fillBackground {
    from {background: red;}
  to {background: blue;}
}
`;


const MainContent = (): JSX.Element => {
    
     const onChange = (a: any, b?: any, c?: any) :void  => {
        console.log(a, b, c);
      }
      
  
    return (
        <>
            <StyledCarousel afterChange={onChange}
                autoplay
            >
 
                <Image
                  src={crossFit}
                />
                
                <Image
                  src={dumbBell}
                />
                 <Image
                  src={weightLift}
                />

            </StyledCarousel>
        </>
    );
}

export default MainContent;