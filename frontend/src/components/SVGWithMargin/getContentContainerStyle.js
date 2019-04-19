
import getMarginObject from './getMarginObject';

type Params = {
  margin: Object | number,
};

export default function getContentContainerStyle({
  margin,
}: Params) {
  const marginObject = getMarginObject(margin);

  return {
    transform: `translate(${marginObject.left}px, ${marginObject.top}px)`,
  };
};
