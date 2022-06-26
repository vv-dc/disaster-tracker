export const PopupWindow = (name, url) => {
  const params = `height=${screen.height},width=${screen.width / 2}`;
  const popup = window.open(url, name, params);

  return new Promise((resolve) => {
    const timer = setInterval(() => {
      try {
        const location = popup.location;
        if (location.href !== url && location.pathname !== 'blank') {
          const urlParams = new URLSearchParams(location.search);
          resolve(Object.fromEntries(urlParams));
          popup.close();
          clearInterval(timer);
        }
      } catch {
        // no-op
      }
    }, 250);
  });
};
